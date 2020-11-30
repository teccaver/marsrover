package marsrover.images.mdkinsey.domain;


import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;

/**
 * Describes an image but is not the image binary data.
 * This is used as a sort of meta-data to describe the image the actual physcial hardware used to
 * create the actual binary image its published URL and local state of if it is currently cached and if so what name
 * it is locally cached as.
 *
 * The relationship to the hardware elements (rover and camera) are not by object references.
 * This is because the image data is retrieved from NASA indepentently of the harward devices which
 * own this image. This allows for inserting the Image without first having to read the two hardware tables rover and camera.
 *
 * Saving to image can not properly use the save or saveAll if the entry already exists. It can not merge or update the
 * row. The cache field may be overwritten. Once the image has a row in this table, it should only be updated specifically if
 * one of the fields describing its local state changes- i.e. cached or cachedFile.
 *
 * Updating it from information retrieved from the NASA api call will overwrite the local state information.
 */
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private int photoId;
    @NonNull
    private int solDate;
    @NonNull
    private String img_src;
    @NonNull
    private String earth_date;
    @JoinColumn(name="rover",insertable = false,updatable = false)
    @NonNull
    private String roverName;
    @NonNull
    @JoinColumns({@JoinColumn(name="camera", referencedColumnName="cameraName",insertable = false, updatable = false),
        @JoinColumn(name="rover", referencedColumnName="rover",insertable = false, updatable = false)})
    private String cameraName;
    private boolean cached;
    private String cachedFile;

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {return id; }

    public int getPhotoId() {
        return photoId;
    }
    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    public String getEarth_date() {
        return earth_date;
    }

    public void setEarth_date(String earth_date) {
        this.earth_date = earth_date;
    }

    public int getSolDate() {
        return solDate;
    }

    public void setSolDate(int solDate) {
        this.solDate = solDate;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getCameraName() { return cameraName; }

    public void setCameraName(String cameraName) {this.cameraName = cameraName;}
    public void setCachedFile(String cachedFile){this.cachedFile = cachedFile;}
    public String getCachedFile(){return cachedFile;}


    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", photoId=" + photoId +
                ", solDate=" + solDate +
                ", img_src='" + img_src + '\'' +
                ", earth_date='" + earth_date + '\'' +
                ", roverName='" + roverName + '\'' +
                ", cameraName='" + cameraName + '\'' +
                ", cached=" + cached +
                ", cachedFile='" + cachedFile + '\'' +
                '}';
    }
}
