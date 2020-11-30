package marsrover.images.mdkinsey.converters;

import marsrover.images.mdkinsey.domain.Camera;
import marsrover.images.mdkinsey.domain.CameraData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class CameraToCameraData implements Converter<Camera, CameraData> {

    public CameraData convert(Camera c){
        CameraData cd = new CameraData();
        cd.setId(c.getId());
        cd.setCameraId(c.getCameraId());
        cd.setCameraName(c.getCameraName());
        cd.setRover(c.getRover());
        return cd;
    }
}
