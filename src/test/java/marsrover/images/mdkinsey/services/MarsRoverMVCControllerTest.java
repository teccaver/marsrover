package marsrover.images.mdkinsey.services;

import marsrover.images.mdkinsey.controllers.ImageIndexController;
import marsrover.images.mdkinsey.converters.CameraJsonToCameraJPA;
import marsrover.images.mdkinsey.converters.ImageJsonToImageJPA;
import marsrover.images.mdkinsey.converters.ImageToImageData;
import marsrover.images.mdkinsey.converters.PhotosJsonToPhotosJPA;
import marsrover.images.mdkinsey.converters.RoverJsonToRoverJPA;
import marsrover.images.mdkinsey.domain.CameraJson;
import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.ImageJson;
import marsrover.images.mdkinsey.domain.RoverJson;
import marsrover.images.mdkinsey.domain.RoverPhotoManifest;
import marsrover.images.mdkinsey.services.repository.ImageServiceRepo;
import marsrover.images.mdkinsey.services.repository.RoverServiceRepo;
import marsrover.images.mdkinsey.types.types;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes={ImageJsonToImageJPA.class, CameraJsonToCameraJPA.class, RoverJsonToRoverJPA.class,
                         PhotosJsonToPhotosJPA.class, ImageToImageData.class})
public class MarsRoverMVCControllerTest {



    private MockMvc mockMvc;

    @InjectMocks
    ImageIndexController imageIndexController;
    @MockBean
    public MarsRoverImageIndexClient marsRoverImageIndexClient;
    @MockBean
    public RoverServiceRepo roverService;
    @MockBean
    public ImageServiceRepo mockImageService;
    @Autowired
    ImageJsonToImageJPA imageJsonToImageJPA;

    ImageToImageData imageToImageData;

    @Autowired
    public void setImageToImageData(ImageToImageData imageToImageData){
        this.imageToImageData = imageToImageData;
    }

    private static TestData[] testData = buildTestData();


    @BeforeEach
    public  void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(imageIndexController).build();
        imageIndexController.setMarsRoverImageIndexClient(marsRoverImageIndexClient);
        imageIndexController.setImageJsonToImageJPA(imageJsonToImageJPA);
        imageIndexController.setImageServiceRepo(mockImageService);
        imageIndexController.setRoverServiceRepo(roverService);
        imageIndexController.setImageToImageData(imageToImageData);

    }

    private static TestData[] buildTestData(){
        TestData[] testSet = new TestData[1];
        TestData t1 = new TestData();
        t1.camera = types.ROVER_CAMERA.NAVCAM;
        t1.rover = types.ROVER.Opportunity;
        t1.solDate = 5;
        t1.pageIndex = 0;
        t1.photoId = 1;
        t1.url = "https://a.b.com/dummyUrl";

        testSet[0] = t1;
        return testSet;
    }

    private static RoverPhotoManifest buildImageList(TestData testData){
        List<ImageJson> imageList = new ArrayList<>();
        RoverPhotoManifest photoManifest = new RoverPhotoManifest();
        photoManifest.setPhotos(imageList);
        ImageJson image1 = new ImageJson();
        CameraJson cameraImage1 = new CameraJson();
        cameraImage1.setName(testData.camera.name());
        cameraImage1.setFull_name(testData.camera.getDescription());
        cameraImage1.setRover_id(testData.rover.ordinal());
        image1.setCamera(cameraImage1);
        image1.setSol(testData.solDate);
        image1.setId(testData.photoId);
        image1.setImg_src(testData.url);
        RoverJson roverJ = buildRover(testData.rover.name());
        image1.setRover(roverJ);
        imageList.add(image1);

        return photoManifest;
    }

    private static RoverJson buildRover(String name){
        RoverJson r = new RoverJson();
        r.setName(name);
        r.setMax_sol(50);
        r.setMax_date("2014-10-15");
        r.setLaunch_date("2003-07-07");
        r.setLanding_date("2004-01-25");
        r.setTotal_photos(198439);
        r.setId(1);
        types.ROVER rover = types.ROVER.valueOf(name);
        types.ROVER_CAMERA[] cameras = rover.getAvailableCamera();
        for (types.ROVER_CAMERA rc : cameras){
            CameraJson camera = new CameraJson();
            camera.setRover_id(rover.ordinal());
            camera.setFull_name(rc.getDescription());
            camera.setName(rc.name());
        }

        return r;
    }


    /**
     * rough test to see if the view fragment is populated
     * @throws Exception
     */
    @Test
    public void testBuildImageListNoneCached() throws Exception{
        TestData t1 = testData[0];
        RoverPhotoManifest photoManifest = buildImageList(t1);
        List<Image> imageList = convertImageJsonList(photoManifest.getPhotos());
        when(marsRoverImageIndexClient.getImagesBySolDate(t1.rover, t1.camera, t1.solDate, t1.pageIndex)).thenReturn(photoManifest);
        //returning null indicates there are non which are cached
        when(mockImageService.findImageByPhotoId(t1.photoId)).thenReturn((Image)null);
        when(mockImageService.saveAll(anyList())).thenReturn(imageList);
        this.mockMvc.perform(post("/getImages")
                             .content("solDateStart=5&cameras%5B%5D=NAVCAM&roverName=Opportunity")
                             .contentType("application/x-www-form-urlencoded; charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(view().name("fragment/fragments::imageTable"))
                .andExpect(model().attribute("imageData", IsCollectionWithSize.hasSize(1)));
    }

    static class TestData{
        types.ROVER rover;
        types.ROVER_CAMERA camera;
        int solDate;
        String landingData="2010-10-10";
        String launchData = "2009-01-01";
        int masSolData=100;
        String maxEarthData="2016-05-05";
        int totalPhotos =100;

        int pageIndex;

        int photoId;
        String url;
    }

    private List<Image> convertImageJsonList(List<ImageJson> imageJ){
        return imageJ.stream().map( (i) -> imageJsonToImageJPA.convert(i))
                                    .collect(Collectors.toList());
    }
}
