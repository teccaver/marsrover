package marsrover.images.mdkinsey.services;

import marsrover.images.mdkinsey.domain.Image;
import marsrover.images.mdkinsey.domain.RoverManifest;
import marsrover.images.mdkinsey.domain.RoverPhotoManifest;
import marsrover.images.mdkinsey.types.types;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface ImageIndexService extends types{
    public static String API_KEY ="4TSHyrCqAIRu5vviDIxUEbFvzXVdVtAyK5aOLMum";


    public default List<String> getCameraList(){
        types.ROVER_CAMERA[] cameras = ROVER_CAMERA.values();
        LinkedList<String> cameraList = new LinkedList(Arrays.asList(cameras));
        return cameraList;
    }

    public Map<ROVER, RoverManifest> getRoverList();

    /**
     *
     * @param rover The rover to query the pictures
     * @param camera Optional camera. Set to null to obtain all photos for this rover
     * @param sol_date The day of operation. Set to null to obtain the all the matched criteria for all days.
     * @param page The limit is 25 per page.
     * @return RoverPhotoManifest containing the photos within the page which matched the given criteria.
     */
    public RoverPhotoManifest getImagesBySolDate(ROVER rover, ROVER_CAMERA camera, int sol_date, int page);
    /**
     *
     * @param rover The rover to query the pictures
     * @param camera Optional camera. Set to null to obtain all photos for this rover matching the remaining criteria.
     * @param earthDate The day of operation. Format YYY-MM-DD. Set to null to obtain photos .
     * @param page The limit is 25 per page.
     * @return RoverPhotoManifest containing the photos within the page which matched the given criteria.
     */
    public RoverPhotoManifest getImagesByEarthDate(ROVER rover, ROVER_CAMERA camera, String earthDate, int page);
    public List<Image> retrieveImages(List<Image> images);


}
