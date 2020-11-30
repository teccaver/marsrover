package marsrover.images.mdkinsey.domain;

import java.util.List;

public class RoverPhotoManifest {

    private List<ImageJson> photos;

    public List<ImageJson> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageJson> photos) {
        this.photos = photos;
    }
}
