package marsrover.images.mdkinsey.converters;

import marsrover.images.mdkinsey.domain.*;
import marsrover.images.mdkinsey.types.types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoverJsonToRoverJPA implements Converter<RoverJson, Rover> {

    @Autowired
    CameraJsonToCameraJPA cameraConverter;
    @Autowired
    PhotosJsonToPhotosJPA photosJsonToPhotos;

    @Override
    public Rover convert(RoverJson r) {
        Rover rover = new Rover();
        rover.setRover_id(r.getId());
        rover.setName(r.getName());
        rover.setStatus(r.getStatus());
        rover.setTotal_photos(r.getTotal_photos());
        rover.setLanding_date(r.getLanding_date());
        rover.setLaunch_date(r.getLaunch_date());
        rover.setMax_date(r.getMax_date());
        rover.setMax_sol(r.getMax_sol());
        types.ROVER rov = types.ROVER.valueOf(r.getName());
        types.ROVER_CAMERA[] cameras = rov.getAvailableCamera();
        for (types.ROVER_CAMERA rcam :cameras){
            Camera c = new Camera();
            c.setCameraName(rcam.name());
            rover.addCamera(c);
        }
        List<PhotosJson> photoJson = r.getPhotos();
        List<Photos> photoList = new ArrayList<>();
        rover.setPhotos(photoList);
        for (PhotosJson pj : photoJson){
            Photos p = photosJsonToPhotos.convert(pj);
            p.setRoverName(rover.getName());
            photoList.add(p);
        }

        return rover;
    }
}
