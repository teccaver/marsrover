package marsrover.images.mdkinsey.converters;

import marsrover.images.mdkinsey.domain.Photos;
import marsrover.images.mdkinsey.domain.PhotosJson;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class PhotosJsonToPhotosJPA implements Converter<PhotosJson, Photos> {


    @Override
    public Photos convert(PhotosJson pj) {
        Photos p = new Photos();
        p.setEarthDate(pj.getEarth_date());
        p.setSolDate(pj.getSol());
        p.setTotalPhotos(pj.getTotal_photos());

        return p;
    }
}
