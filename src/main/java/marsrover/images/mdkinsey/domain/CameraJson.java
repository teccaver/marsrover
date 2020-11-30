package marsrover.images.mdkinsey.domain;


public class CameraJson {

    private int id;
    private String name;
    private int rover_id;
    private String full_name;

    public Integer getId() {
        return id;
    }

    public int getRover_id() {
        return rover_id;
    }

    public void setRover_id(int rover_id) {
        this.rover_id = rover_id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getFull_name() { return full_name; }

    public void setFull_name(String full_name) { this.full_name = full_name; }

}
