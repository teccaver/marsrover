package marsrover.images.mdkinsey.domain;

import java.util.ArrayList;
import java.util.List;

public class RoverData {

    private int roverId;
    private String status;
    private String name;
    private String landingDate;
    private String launchDate;
    private int maxSol;
    private String maxDate;
    private int totalPhotos;
    private List<CameraData> cameras = new ArrayList<>();
    private String cameraDisplay="";

    public int getRoverId() {
        return roverId;
    }

    public void setRoverId(int roverId) {
        this.roverId = roverId;
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
        this.name = name;
    }

    public String getLandingDate() {
        return landingDate;
    }

    public void setLandingDate(String landingDate) {
        this.landingDate = landingDate;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public int getMaxSol() {
        return maxSol;
    }

    public void setMaxSol(int maxSol) {
        this.maxSol = maxSol;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public List<CameraData> getCameras() {
        return cameras;
    }

    public void setCameras(List<CameraData> cameras) {

        this.cameras = cameras;
        for (CameraData c:cameras){
            if (cameraDisplay.length()>0){
                cameraDisplay+=", "+c.getCameraName();
            }
            else{
                cameraDisplay+=c.getCameraName();
            }
        }
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public String getCameraDisplay() {
        return cameraDisplay;
    }

    @Override
    public String toString() {
        return "RoverData{" +
                "roverId=" + roverId +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", landingDate='" + landingDate + '\'' +
                ", launchDate='" + launchDate + '\'' +
                ", maxSol=" + maxSol +
                ", maxDate='" + maxDate + '\'' +
                ", totalPhotos=" + totalPhotos +
                ", cameraList=" + cameras +
                '}';
    }
}
