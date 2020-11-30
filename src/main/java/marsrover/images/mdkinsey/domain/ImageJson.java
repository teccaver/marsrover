package marsrover.images.mdkinsey.domain;


public class ImageJson {

    private int id;
    private int sol;
    private String img_src;
    private String earth_date;
    private RoverJson rover;
    private CameraJson camera;
    private boolean cached;

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public String getEarth_date() {
        return earth_date;
    }

    public void setEarth_date(String earth_date) {
        this.earth_date = earth_date;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSol() {
        return sol;
    }

    public void setSol(int sol) {
        this.sol = sol;
    }

    public RoverJson getRover() {
        return rover;
    }

    public void setRover(RoverJson rover) {
        this.rover = rover;
    }

    public CameraJson getCamera() {
        return camera;
    }

    public void setCamera(CameraJson camera) {
        this.camera = camera;
    }
}
