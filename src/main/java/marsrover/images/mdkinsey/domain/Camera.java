package marsrover.images.mdkinsey.domain;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "rover_camera", columnNames = {"cameraName","rover"}))
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private int cameraId;
    private String cameraName;
    private Integer rover_id;
    @ManyToOne
    @JoinColumn(name="rover")
    private Rover rover;
    private String description;

    transient private int page;
    transient private int pageSize;

    public int getPage() {
        return page;
    }

    public Integer getId() {
        return id;
    }

    public Rover getRover() {
        return rover;
    }

    public void setRover(Rover rover) {
        this.rover = rover;
    }

    public Integer getRover_id() {
        return rover_id;
    }

    public void setRover_id(Integer rover_id) {
        this.rover_id = rover_id;
    }

    public void setId(Integer id) { this.id = id; }

    public int getCameraId() { return cameraId; }

    public void setCameraId(int cameraId) { this.cameraId = cameraId; }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCameraName() { return cameraName; }

    public void setCameraName(String name) { this.cameraName = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

}
