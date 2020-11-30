package marsrover.images.mdkinsey.converters;

import marsrover.images.mdkinsey.domain.Camera;
import marsrover.images.mdkinsey.domain.CameraJson;
import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.ImageJson;
import marsrover.images.mdkinsey.domain.Rover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageJsonToImageJPA implements Converter<ImageJson, Image> {
    @Autowired
    private CameraJsonToCameraJPA cameraConverter;
    @Autowired
    private RoverJsonToRoverJPA roverConverter;

    @Override
    public Image convert(ImageJson imageJson) {
        Image image = new Image();
        CameraJson cj = imageJson.getCamera();
        Camera c = cameraConverter.convert(cj);
        image.setCameraName(c.getCameraName());
        image.setPhotoId(imageJson.getId());
        image.setEarth_date(imageJson.getEarth_date());
        image.setSolDate(imageJson.getSol());
        image.setImg_src(imageJson.getImg_src());
        Rover rover = roverConverter.convert(imageJson.getRover());
        image.setRoverName(rover.getName());
        return image;
    }
}
