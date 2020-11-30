package marsrover.images.mdkinsey.domain;

import marsrover.images.mdkinsey.types.types;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This with the camera table model the hardware with which the library of photos was taken. It essentiall provides the
 * criteria for search through the library of 100,000s of photos by device (rover, camera) and date (sol, earth).
 * The two hardware devices form a composition which together uniquely define a criteria for a set of images.
 *
 */
@Entity
public class Rover {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;
    private int rover_id;
    private String status;
    @Id
    @Column(name = "name")
    private String name;
    private String landing_date;
    private String launch_date;
    private int max_sol;
    private String max_date;
    private int total_photos;
    //photos is part of snap shots but not included when the rover is added
    //@OneToMany(cascade = CascadeType.ALL)
    transient private List<Photos> photos;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Camera> cameras = new ArrayList<>();

    @Override
    public String toString() {
        return "Rover{" +
                "rover_id=" + rover_id +
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

    public Rover(){}

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
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


//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

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
        String rover = types.ROVER.toDB(name);
        this.name = rover;
    }

    public int getRover_id() {
        return rover_id;
    }

    public void setRover_id(int roverId) {
        this.rover_id = roverId;
    }

    public StringBuilder toString(StringBuilder sb){
        sb.append(toString());
        return sb;
    }

    public void addCamera(Camera camera){
        this.cameras.add(camera);
    }
}
