package marsrover.images.mdkinsey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import marsrover.images.mdkinsey.types.types;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has dual representation. It serves as the defintion of the rover manifest as retrieved from NASA
 * and as the stored entity which defines the data model for the rover.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoverJson {

    private int id;
    private String status;
    private String name;
    private String landing_date;
    private String launch_date;
    private int max_sol;
    private String max_date;
    private int total_photos;
    //photos is part of snap shots but not included when the rover is added
    private List<PhotosJson> photos = new ArrayList<>();
    private List<CameraJson> cameras = new ArrayList<>();

    @Override
    public String toString() {
        return "RoverJson{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", landing_date='" + landing_date + '\'' +
                ", launch_date='" + launch_date + '\'' +
                ", max_sol=" + max_sol +
                ", max_date='" + max_date + '\'' +
                ", total_photos=" + total_photos +
                ", photos=" + photos +
                ", cameras=" + cameras +
                '}';
    }

    public RoverJson(){}

    public List<CameraJson> getCameras() {
        return cameras;
    }

    public void setCameras(List<CameraJson> cameras) {
        this.cameras = cameras;
    }

    public List<PhotosJson> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotosJson> photos) {
        this.photos = photos;
    }

    public String getLanding_date() {
        return landing_date;
    }

    public void setLanding_date(String landing_date) {
        this.landing_date = landing_date;
    }

    public String getLaunch_date() {
        return launch_date;
    }

    public void setLaunch_date(String launch_date) {
        this.launch_date = launch_date;
    }

    public int getMax_sol() {
        return max_sol;
    }

    public void setMax_sol(int max_sol) {
        this.max_sol = max_sol;
    }

    public String getMax_date() {
        return max_date;
    }

    public void setMax_date(String max_date) {
        this.max_date = max_date;
    }

    public int getTotal_photos() {
        return total_photos;
    }

    public void setTotal_photos(int total_photos) {
        this.total_photos = total_photos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = types.ROVER.toDB(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int roverId) {
        this.id = roverId;
    }

    public StringBuilder toString(StringBuilder sb){
        sb.append(toString());
        return sb;
    }

}
