package marsrover.images.mdkinsey.services;

import marsrover.images.mdkinsey.controllers.ImageIndexController;
import marsrover.images.mdkinsey.converters.ImageJsonToImageJPA;
import marsrover.images.mdkinsey.domain.CameraJson;
import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.ImageJson;
import marsrover.images.mdkinsey.domain.RoverJson;
import marsrover.images.mdkinsey.domain.RoverPhotoManifest;
import marsrover.images.mdkinsey.services.repository.ImageServiceRepo;
import marsrover.images.mdkinsey.services.repository.RoverServiceRepo;
import marsrover.images.mdkinsey.types.types;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//@SpringBootTest
@WebMvcTest
//@RestClientTest({MarsRoverImageIndexClient.class})
//@MockBean({RoverServiceRepo.class,ImageServiceRepo.class, RestTemplateBuilder.class})

@ExtendWith(MockitoExtension.class)
@Disabled
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
    @MockBean
    RestTemplateBuilder restTemplateBuilder;
    @Autowired
    ImageJsonToImageJPA imageJsonToImageJPA;

    private static TestData[] testData = buildTestData();


    @BeforeEach
    public  void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(new ImageIndexController()).build();
        imageIndexController.setMarsRoverImageIndexClient(marsRoverImageIndexClient);

    }

    private static TestData[] buildTestData(){
        TestData[] testSet = new TestData[1];
        TestData t1 = new TestData();
        t1.camera = types.ROVER_CAMERA.NAVCAM;
        t1.rover = types.ROVER.Opportunity;
        t1.solDate = 5;
        t1.pageIndex = 0;

        testSet[0] = t1;
        return testSet;
    }

    private static RoverPhotoManifest buildImageList(TestData testData){
        List<ImageJson> imageList = new ArrayList<>();
        RoverPhotoManifest photoManifest = new RoverPhotoManifest();
        photoManifest.setPhotos(imageList);
        ImageJson image1 = new ImageJson();
        CameraJson cameraImage1 = new CameraJson();
        cameraImage1.setName(testData.rover.name());
        cameraImage1.setFull_name(testData.camera.getDescription());
        cameraImage1.setRover_id(testData.rover.ordinal());
        image1.setCamera(cameraImage1);
        image1.setSol(testData.solDate);
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


    @Test
    public void testBuildImageListNoneCached() throws Exception{
        TestData t1 = testData[0];
        RoverPhotoManifest photoManifest = buildImageList(t1);
        List<Image> imageList = convertImageJsonList(photoManifest.getPhotos());

        Mockito.when(marsRoverImageIndexClient.getImagesBySolDate(t1.rover, t1.camera, t1.solDate, t1.pageIndex)).thenReturn(photoManifest);
        //returning null indicates there are non which are cached
        Mockito.when(mockImageService.findImageByPhotoId(t1.photoId)).thenReturn((Image)null);
        Mockito.when(mockImageService.saveAll(imageList)).thenReturn(imageList);
        this.mockMvc.perform(post("/getImages")
                             .content("solDateStart=1&cameras%5B%5D=NAVCAM&roverName=opportunity")
                             .contentType("application/x-www-form-urlencoded; charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(view().name("fragment/fragments::imageTable"))
                .andExpect(model().attribute("imageData", Matchers.arrayWithSize(1)));
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
    }

    private List<Image> convertImageJsonList(List<ImageJson> imageJ){
        return imageJ.stream().map( (i) -> imageJsonToImageJPA.convert(i))
                                    .collect(Collectors.toList());
    }
}
