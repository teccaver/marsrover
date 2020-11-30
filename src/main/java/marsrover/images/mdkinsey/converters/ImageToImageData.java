package marsrover.images.mdkinsey.converters;

import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.ImageData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageToImageData implements Converter<Image, ImageData> {
    @Override
    public ImageData convert(Image image) {
        ImageData id = new ImageData();
        id.setCached(image.isCached());
        id.setUrl(image.getImg_src());
        id.setImageId(image.getId());
        id.setPhotoId(image.getPhotoId());
        id.setSolDate(image.getSolDate());
        id.setEarthDate(image.getEarth_date());
        id.setCamera(image.getCameraName());
        id.setRoverName(image.getRoverName());

        return id;
    }
}
