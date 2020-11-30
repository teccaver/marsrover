package marsrover.images.mdkinsey.converters;

import marsrover.images.mdkinsey.domain.Camera;
import marsrover.images.mdkinsey.domain.CameraJson;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CameraJsonToCameraJPA implements Converter<CameraJson, Camera> {
    @Override
    public Camera convert(CameraJson c) {
        Camera camera = new Camera();
        camera.setCameraId(c.getId());
        camera.setDescription(c.getFull_name());
        camera.setCameraName(c.getName());

        return camera;
    }
}
