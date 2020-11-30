package marsrover.images.mdkinsey.services;

import marsrover.images.mdkinsey.converters.CameraJsonToCameraJPA;
import marsrover.images.mdkinsey.converters.ImageJsonToImageJPA;
import marsrover.images.mdkinsey.converters.RoverJsonToRoverJPA;
import marsrover.images.mdkinsey.domain.Camera;
import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.ImageJson;
import marsrover.images.mdkinsey.domain.Rover;
import marsrover.images.mdkinsey.domain.RoverManifest;
import marsrover.images.mdkinsey.domain.RoverPhotoManifest;
import marsrover.images.mdkinsey.services.repository.ImageServiceRepo;
import marsrover.images.mdkinsey.services.repository.RoverServiceRepo;
import marsrover.images.mdkinsey.types.types;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


//@ContextConfiguration(classes = {MarsRoverImageIndex.class,RestTemplate.class})
//@WebMvcTest
//@AutoConfigureMockMvc
@SpringBootTest
public class MarsRoverJPARepositoryTest {

    @Autowired
    private MarsRoverImageIndexClient marsRoverImageIndex;
    @Autowired
    private RoverServiceRepo roverServiceRepo;
    @Autowired
    private ImageServiceRepo imageServiceRepo;

    @Autowired
    private CameraJsonToCameraJPA cameraJsonConverter;
    @Autowired
    private RoverJsonToRoverJPA roverJsonConverter;
    @Autowired
    private ImageJsonToImageJPA imageJsonConverter;

    @BeforeEach
    void setup(){
        Map<types.ROVER, RoverManifest> roverMap = marsRoverImageIndex.getRoverList();
        for (Map.Entry<types.ROVER, RoverManifest> e:roverMap.entrySet()){
            System.out.println("Adding: "+e.getValue().getPhoto_manifest().getName());
            Rover rover = roverJsonConverter.convert(e.getValue().getPhoto_manifest());
            Rover savedRover = roverServiceRepo.save(rover);
            List<Camera> cameraList = savedRover.getCameras();
            for (Camera c : cameraList){
                assertThat(c.getId()).isGreaterThan(0);
            }
        }
        System.out.println("Rovers and Cameras successfully added");
    }

    @AfterEach
    void deleteAllImages(){
        imageServiceRepo.deleteAll();
    }

    @Test
    void storeAndRetieveImage(){
        //YYYY-MM-DD
        String date = "2012-08-07";
        RoverPhotoManifest photoManifest = marsRoverImageIndex.getImagesByEarthDate(types.ROVER.Curiosity, (types.ROVER_CAMERA)null, date, 0);
        List<ImageJson> imageJsonList = photoManifest.getPhotos();
        List<Image> imageList = convertImages(imageJsonList);
        imageServiceRepo.saveAll(imageList);
        Iterable<Image> savedList = imageServiceRepo.findAll();
        savedList.forEach(x ->System.out.println(x.getId()+" photoID: "+x.getPhotoId()+"  : "+x.getImg_src()));
        List<Image> savedImgList = new ArrayList<Image>((Collection<? extends Image>) savedList);
        assertThat(savedImgList.size()).isEqualTo(imageList.size());

    }


    @Test
    void cacheImage(){
        String date = "2012-08-07";
        RoverPhotoManifest photoManifest = marsRoverImageIndex.getImagesByEarthDate(types.ROVER.Curiosity, (types.ROVER_CAMERA)null, date, 0);
        List<ImageJson> imageJsonList = photoManifest.getPhotos();
        List<Image> imageList = convertImages(imageJsonList);
        imageServiceRepo.saveAll(imageList);
        Iterable<Image> savedList = imageServiceRepo.findAll();
        savedList.forEach(x ->System.out.println(x.getId()+" photoID: "+x.getPhotoId()+"  : "+x.getImg_src()));
        List<Image> savedImgList = new ArrayList<Image>((Collection<? extends Image>) savedList);
        assertThat(savedImgList.size()).isEqualTo(imageList.size());
        //retrieve the image list
        List<Image> retrievedImageList = marsRoverImageIndex.retrieveImages(savedImgList);
        retrievedImageList.forEach(x -> System.out.println(x.getId()+"  "+x.getPhotoId()+"  "+x.getCachedFile()+"  "+x.isCached()));
    }

    /**
     * requires manually inspecting the logs
     */
    @Test
    void cacheHit(){
        String date = "2012-08-07";
        RoverPhotoManifest photoManifest = marsRoverImageIndex.getImagesByEarthDate(types.ROVER.Curiosity, (types.ROVER_CAMERA)null, date, 0);
        List<ImageJson> imageJsonList0 = photoManifest.getPhotos();
        List<ImageJson> imageJsonList = new ArrayList<>();
        imageJsonList.add(imageJsonList0.get(0));
        imageJsonList.add(imageJsonList0.get(1));
        imageJsonList.add(imageJsonList0.get(2));

        List<Image> imageList1 = convertImages(imageJsonList);
        List<Image> list1 = (List<Image>) imageServiceRepo.saveAll(imageList1);
        RoverPhotoManifest photoManifest1 = marsRoverImageIndex.getImagesBySolDate(types.ROVER.Opportunity, (types.ROVER_CAMERA)null, 50, 0);
        imageJsonList = photoManifest1.getPhotos();
        //only work with the first 5 or so
        List<ImageJson> imageJsonList2 = new ArrayList<ImageJson>();
        imageJsonList2.add(imageJsonList.get(0));
        imageJsonList2.add(imageJsonList.get(1));
        imageJsonList2.add(imageJsonList.get(2));
        List<Image> imageList2 = convertImages(imageJsonList2);
        List<Image> list2 = (List<Image>) imageServiceRepo.saveAll(imageList2);

        //seed the cache
        List<Image> retrievedImageList = marsRoverImageIndex.retrieveImages(list1);
        list2.add(0, list1.get(0));
        list2.add(0, list1.get(1));
        list2.add(0, list1.get(2));

        List<Image> retrievedList = marsRoverImageIndex.retrieveImages(list2);
        //check logs to see if the first 3 files of list 2 were not retrieved because they were already cached.


        //use some of both

    }

    private List<Image> convertImages(List<ImageJson> imageJsonList){
        List<Image> imageList = new ArrayList<>();
        for (ImageJson imageJson:imageJsonList){
            Image image = imageJsonConverter.convert(imageJson);
            imageList.add(image);
        }
        return imageList;
    }
}
