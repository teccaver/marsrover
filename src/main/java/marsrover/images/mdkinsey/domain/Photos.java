package marsrover.images.mdkinsey.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Photos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String roverName;
    private int solDate;
    private String earthDate;
    private int totalPhotos;
    private String cameraName;

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    public int getSolDate() {
        return solDate;
    }

    public void setSolDate(int solDate) {
        this.solDate = solDate;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public String getCameras() {
        return cameraName;
    }

    public void setCameras(String cameraName) {
        this.cameraName = cameraName;
    }

    @Override
    public String toString() {
        return "Photos{" +
                "roverName='" + roverName + '\'' +
                ", solDate=" + solDate +
                ", earthDate='" + earthDate + '\'' +
                ", totalPhotos=" + totalPhotos +
                ", cameras=" + cameraName +
                '}';
    }
}
