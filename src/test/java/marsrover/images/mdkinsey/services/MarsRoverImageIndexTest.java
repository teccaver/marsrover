package marsrover.images.mdkinsey.services;

import marsrover.images.mdkinsey.domain.RoverManifest;
import marsrover.images.mdkinsey.domain.RoverPhotoManifest;
import marsrover.images.mdkinsey.types.types;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@ContextConfiguration(classes = {MarsRoverImageIndexClient.class, RestTemplate.class})
@WebMvcTest
@AutoConfigureMockMvc
class MarsRoverImageIndexTest {

    private MarsRoverImageIndexClient imageIndex;

    @Autowired
    public void setMarsRoverImageIndex(MarsRoverImageIndexClient imageIndex){
        this.imageIndex = imageIndex;
    }


    @Test
    void getRoverList() {
        Map<types.ROVER, RoverManifest> roverMap = imageIndex.getRoverList();
        assert(roverMap.containsKey(types.ROVER.Curiosity));
        assert(roverMap.containsKey(types.ROVER.Opportunity));
        assert(roverMap.containsKey(types.ROVER.Spirit));
        for (Map.Entry<types.ROVER, RoverManifest> e:roverMap.entrySet()){
            assert(e.getValue().getPhoto_manifest().getTotal_photos()>0);
        }
    }

    @Test
    void getImagesBySolDateCamera() {
        RoverPhotoManifest photoManifest = imageIndex.getImagesBySolDate(types.ROVER.Curiosity, types.ROVER_CAMERA.FHAZ, 1004, 0);
        System.out.println("Number of photos by rover: "+photoManifest.getPhotos().size());
        assert(photoManifest.getPhotos().get(0).getSol()==1004);
    }

    @Test
    void getImagesBySolDate(){
        RoverPhotoManifest photoManifest = imageIndex.getImagesBySolDate(types.ROVER.Curiosity, (types.ROVER_CAMERA)null, 1004, 0);
        System.out.println("Number of photos by rover: "+photoManifest.getPhotos().size());
        assert(photoManifest.getPhotos().get(0).getSol()==1004);
    }

    @Test
    void getImagesByEarthDateCamera() {
        String date = "2004-01-05";
        RoverPhotoManifest photoManifest = imageIndex.getImagesByEarthDate(types.ROVER.Spirit, types.ROVER_CAMERA.FHAZ, date, 0);
        System.out.println("Number of photos by rover: "+photoManifest.getPhotos().size());
        assert(photoManifest.getPhotos().get(0).getEarth_date().equals(date));
    }

    @Test
    void getImagesByEarthDate(){
        String date = "2012-08-07";
        RoverPhotoManifest photoManifest = imageIndex.getImagesByEarthDate(types.ROVER.Curiosity, (types.ROVER_CAMERA)null, date, 0);
        System.out.println("Number of photos by rover: "+photoManifest.getPhotos().size());
        assert(date.equals(photoManifest.getPhotos().get(0).getEarth_date()));
    }


}