package marsrover.images.mdkinsey.services;

import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.Rover;
import marsrover.images.mdkinsey.types.types;

import java.util.List;

public interface MarsRoverFileStore {
    public static final String STOREPATH="storepath";

    public boolean roverExists();
    public boolean photoExists();
    public Image getPhotoById();
    public List<Image> getImageByCamera(types.ROVER rover, String camera);
    public List<Image> getImageBySolData(types.ROVER rover, String camera, int solDate);
    public void saveSnapShot(Rover rover);
}
