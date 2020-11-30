package marsrover.images.mdkinsey.domain;

public class CameraData {

    private Integer id;
    private int cameraId;
    private String cameraName;
    private Rover Rover;
    private String description;
    private int page;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public marsrover.images.mdkinsey.domain.Rover getRover() {
        return Rover;
    }

    public void setRover(marsrover.images.mdkinsey.domain.Rover rover) {
        Rover = rover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "CameraData{" +
                "id=" + id +
                ", cameraId=" + cameraId +
                ", cameraName='" + cameraName + '\'' +
                ", Rover=" + Rover +
                ", description='" + description + '\'' +
                ", page=" + page +
                '}';
    }
}
