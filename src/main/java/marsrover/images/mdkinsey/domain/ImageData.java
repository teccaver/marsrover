package marsrover.images.mdkinsey.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageData {

    private static final Pattern imageNamePart = Pattern.compile(".*/(.*)$");

    /**internal id*/
    private Integer imageId;
    /**ID assigned by Nasa*/
    private Integer photoId;
    private String url;
    private String camera;
    private boolean cached;
    private Integer solDate;
    private String earthDate;
    private String imageName;
    private String roverName;


    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        Matcher m = imageNamePart.matcher(url);
        if (m.matches() && m.groupCount()>0) {
            imageName = m.group(1);
        }

    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public Integer getSolDate() {
        return solDate;
    }

    public void setSolDate(Integer solDate) {
        this.solDate = solDate;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "imageId=" + imageId +
                ", photoId=" + photoId +
                ", url='" + url + '\'' +
                ", camera='" + camera + '\'' +
                ", cached=" + cached +
                ", solDate=" + solDate +
                ", earthDate='" + earthDate + '\'' +
                ", imageName='" + imageName + '\'' +
                ", roverName='" + roverName + '\'' +
                '}';
    }
}
