package marsrover.images.mdkinsey.converters;

import marsrover.images.mdkinsey.domain.Camera;
import marsrover.images.mdkinsey.domain.CameraData;
import marsrover.images.mdkinsey.domain.Rover;
import marsrover.images.mdkinsey.domain.RoverData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoverToRoverData implements Converter<Rover, RoverData> {

    @Autowired
    private CameraToCameraData cameraToCameraData;
    @Override
    public RoverData convert(Rover rover) {
        RoverData rd = new RoverData();
        rd.setRoverId(rover.getRover_id());
        rd.setLandingDate(rover.getLanding_date());
        rd.setLaunchDate(rover.getLaunch_date());
        rd.setMaxSol(rover.getMax_sol());
        rd.setStatus(rover.getStatus());
        rd.setTotalPhotos(rover.getTotal_photos());
        rd.setName(rover.getName());
        List<Camera> cList = rover.getCameras();
        List<CameraData> cdList = new ArrayList<>();
        for (Camera c : cList){
            CameraData cd = cameraToCameraData.convert(c);
            cdList.add(cd);
        }
        rd.setCameras(cdList);
        rd.setMaxDate(rover.getMax_date());

        return rd;
    }
}
