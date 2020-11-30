package marsrover.images.mdkinsey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Used for parsing the result from the nasa service
 * This is a representation of the set of photos on
 * a specific day. This set of photos can be sourced from
 * multiple cameras on 1 rover on 1 day.
 * This is more of a meta-data describing a day in the photo shoot
 * of a rover.
 *
 * It is obtained and populated when retrieving the rover's manifest
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotosJson {

    private int sol;
    private String earth_date;
    private int total_photos;
    transient private String[] cameras;
    private String roverName;

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    public PhotosJson(){}

    public int getSol() {return sol; }

    public void setSol(int sol) {
        this.sol = sol;
    }

    public String getEarth_date() {
        return earth_date;
    }

    public void setEarth_date(String earth_date) {
        this.earth_date = earth_date;
    }

    public int getTotal_photos() {
        return total_photos;
    }

    public void setTotal_photos(int total_photos) {
        this.total_photos = total_photos;
    }

    public String[] getCameras() {
        return cameras;
    }

    public void setCameras(String[] cameras) {
        this.cameras = cameras;
    }
}
