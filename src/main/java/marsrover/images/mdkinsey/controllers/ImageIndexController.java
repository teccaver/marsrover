package marsrover.images.mdkinsey.controllers;

import marsrover.images.mdkinsey.converters.ImageJsonToImageJPA;
import marsrover.images.mdkinsey.converters.ImageToImageData;
import marsrover.images.mdkinsey.converters.RoverToRoverData;
import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.ImageData;
import marsrover.images.mdkinsey.domain.ImageJson;
import marsrover.images.mdkinsey.domain.ImageSearch;
import marsrover.images.mdkinsey.domain.Rover;
import marsrover.images.mdkinsey.domain.RoverData;
import marsrover.images.mdkinsey.domain.RoverPhotoManifest;
import marsrover.images.mdkinsey.services.MarsRoverImageIndexClient;
import marsrover.images.mdkinsey.services.repository.ImageServiceRepo;
import marsrover.images.mdkinsey.services.repository.RoverServiceRepo;
import marsrover.images.mdkinsey.types.types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ImageIndexController {

    private RoverServiceRepo roverService;
    private ImageServiceRepo imageService;
    private RoverToRoverData roverToRoverData;
    private ImageJsonToImageJPA imageJsonToImageJPA;
    private ImageToImageData imageToImageData;
    private MarsRoverImageIndexClient marsRoverImageIndexClient;

    @Autowired
    public void setRoverServiceRepo(RoverServiceRepo roverServiceRepo){
        this.roverService = roverServiceRepo;
    }
    @Autowired
    public void setImageServiceRepo(ImageServiceRepo imageServiceRepo){
        this.imageService = imageServiceRepo;
    }

    @Autowired
    public void setMarsRoverImageIndexClient(MarsRoverImageIndexClient marsRoverImageIndexClient){
        this.marsRoverImageIndexClient = marsRoverImageIndexClient;
    }

    @Autowired
    public void setImageToImageData(ImageToImageData imageToImageData){
        this.imageToImageData = imageToImageData;
    }

    @Autowired
    public void setImageJsonToImageJPA(ImageJsonToImageJPA imageJsonToImageJPA){
        this.imageJsonToImageJPA = imageJsonToImageJPA;
    }

    @Autowired
    public void setRoverToRoverData(RoverToRoverData roverToRoverData){
        this.roverToRoverData = roverToRoverData;
    }

    /**
     * Sets up the initial view
     * @param model the model
     * @return the initial view with the search form
     */
    @RequestMapping("/")
    public String index(Model model){
        String defaultRoverName = "Opportunity";
        Optional<Rover> rover = roverService.findById(defaultRoverName);
        System.out.println("Retireved rover for index page: "+rover.toString());
        RoverData rd = new RoverData();
        if (rover.isPresent()) {
            rd = roverToRoverData.convert(rover.get());
        }else{
            //TODO: use default values or send back system not ready or a failure
        }
        model.addAttribute("roverData", rd);
        model.addAttribute("imageSearch", new ImageSearch());
        return "index";
    }

    /**
     * Retrieves the information pertaining to a specific rover
     * @param roverName Name of the Rover
     * @param model the model
     * @return the html fragment with rover details
     */
    @RequestMapping("/getRover")
    public String getRover(@RequestParam String roverName, Model model){
        Optional<Rover> rover = roverService.findById(types.ROVER.toDB(roverName));
        System.out.println("/getRover param: "+roverName);
        RoverData rd;
        rd = rover.map(value -> roverToRoverData.convert(value)).orElse(null);
        model.addAttribute("roverData", rd);
        model.addAttribute("imageSearch", new ImageSearch());
        return "fragment/fragments :: roverdetails";
    }

    /**
     * Reloads the search form for a particular rover. The form is cleared.
     * @param roverName name of the rover
     * @param model the model
     * @return A cleared search form fragment
     */
    @RequestMapping("reloadSearchForm")
    public String reloadSearcForm(@RequestParam String roverName, Model model){
        Optional<Rover> rover = roverService.findById(roverName);
        System.out.println("reloadSearchForm param: "+roverName);
        RoverData rd;
        rd = roverToRoverData.convert(rover.get());
        model.addAttribute("roverData", rd);
        model.addAttribute("imageSearch", new ImageSearch());
        return "fragment/fragments :: searchForm";
    }

    /**
     * Retrieves the actual binary file for the image.
     * @param photoId photo ID as assigned by the backend image library
     * @return the image binary data
     */
    @GetMapping(value="/getSelectedImage")
    @ResponseBody
    public ResponseEntity<byte[]> getResponse(@RequestParam Integer photoId){
        System.out.println("getSelectedImage with photoId: "+photoId);
        Image image = imageService.findImageByPhotoId(photoId);
        if (image != null){
            List<Image> imageList = new ArrayList<>();
            imageList.add(image);
            imageList = marsRoverImageIndexClient.retrieveImages(imageList);
            image = imageList.get(0);
            byte[] imageBytes = new byte[0];
            File f = new File(image.getCachedFile());
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(f);
                imageBytes = StreamUtils.copyToByteArray(fileInputStream);
            } catch (FileNotFoundException e) {
                System.out.printf("Image File %s not found in cached",image.getCachedFile());
            }catch(IOException e){
                e.printStackTrace();
            }
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        }
        return ResponseEntity.notFound().build();

    }

    /**
     * Retrieves from the backend rest service a set of meta-data describing a set of images based on the search criteria
     * received from the client.
     * This set is then converted from the json format to the entity class format and saved within the repository. Another
     * conversion to a consumer side format is then made.
     *
     * The intent is to support ranges where subsequent searches may overlap previous searches. Image data which already
     * exists in the persistence store is not updated. This preserves any state changes resulting from client interaction
     * with that specific image.
     *
     * @param imageSearch class containing search criteria
     * @param model the model
     * @return a view fragment containing the set of images meta-data which matches the search criteria
     */
    @RequestMapping(value = "/getImages")
    public String getImages(ImageSearch imageSearch, Model model){
        System.out.println("method getImages:\n"+imageSearch.toString());
        boolean useSolDate = true;
        if (imageSearch.getRoverName()==null){
            //assume default
            imageSearch.setRoverName(types.ROVER.Opportunity.name());
        }
        if (imageSearch.getEarthDateStart() != null && imageSearch.getEarthDateStart().length()=="YYYY-MM-DD".length()){
            //Valid earth date
            useSolDate = false;
        }

        //can have more than 1 camera but only 1 rover
        List<String> cameraNames = imageSearch.getCameras();
        types.ROVER rover = types.ROVER.valueOf(types.ROVER.toDB(imageSearch.getRoverName()));
        List<ImageData> imageDataList = new ArrayList<>();
        if (cameraNames.size()>0){
            //pull down the list of pics for each camera
            for (String cameraName : cameraNames){
                types.ROVER_CAMERA camera = types.ROVER_CAMERA.valueOf(cameraName);
                /* retrieve all meta data for acriteria. unknown if all the data has already been retrieved for this criteria once it is made to search for ranges. */
                RoverPhotoManifest photoManifest;
                if (useSolDate)
                    photoManifest = marsRoverImageIndexClient.getImagesBySolDate(rover, camera, imageSearch.getSolDateStart(), imageSearch.getPage());
                else
                    photoManifest = marsRoverImageIndexClient.getImagesByEarthDate(rover, camera, imageSearch.getEarthDateStart(), imageSearch.getPage());
                //list in received json format
                List<ImageJson> imageJList = photoManifest.getPhotos();
                // list after being saved to the repository
                List<Image> savedImageList = new ArrayList<>();
                // save to the repository
                buildImageForRepository(imageJList, savedImageList);
                // convert from the repository entity to the consumable form
                buildImageForConsumer(savedImageList, imageDataList);
            }
        }
        else{
            //respond with an error- must supply at least 1 camera
            System.out.println("ERROR no camera specified");
        }
        model.addAttribute("imageData",imageDataList);
        model.addAttribute("roverName", types.ROVER.toDB(imageSearch.getRoverName()));
        return "fragment/fragments::imageTable";
    }

    /**
     * @param imageJList a list of image meta-data in JSON format
     * @param c the converted list of Image
     */
    private void buildImageForRepository(List<ImageJson> imageJList, List<Image> c) {

        List<Image> imageRList = new ArrayList<>();
        for (ImageJson ij : imageJList) {
            Image image = imageJsonToImageJPA.convert(ij);
            Image existsImage = imageService.findImageByPhotoId(image.getPhotoId());
            //this changes up the order as more images are cached
            if (existsImage == null) {
                imageRList.add(image);
            }else{
                c.add(existsImage);
            }
        }
        Iterable<Image> saveImageItList = imageService.saveAll(imageRList);
        saveImageItList.forEach(c::add);

    }


    private void buildImageForConsumer(List<Image> imageList, List<ImageData> imageDataList){

        imageDataList.addAll( imageList.stream().map((i)->imageToImageData.convert(i))
                                          .collect(Collectors.toList()) );


    }



}
