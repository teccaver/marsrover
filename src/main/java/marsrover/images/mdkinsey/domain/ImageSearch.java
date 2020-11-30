package marsrover.images.mdkinsey.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * query form data from the index page
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageSearch {
    String roverName;
    @JsonFormat(pattern="yyyy-mm-dd")
    String earthDateStart;
    @JsonFormat(pattern="yyyy-mm-dd")
    String earthDateEnd;
    Integer solDateStart=0;
    int solDateEnd;
    List<String> cameras = new ArrayList<>();
    int page;
    int pageSize = 25;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    public String getEarthDateStart() {
        return earthDateStart;
    }

    public void setEarthDateStart(String earthDateStart) {
        this.earthDateStart = earthDateStart;
    }

    public String getEarthDateEnd() {
        return earthDateEnd;
    }

    public void setEarthDateEnd(String earthDateEnd) {
        this.earthDateEnd = earthDateEnd;
    }

    public Integer getSolDateStart() {
        return solDateStart;
    }

    public void setSolDateStart(Integer solDateStart) {
        this.solDateStart = solDateStart;
    }

    public int getSolDateEnd() {
        return solDateEnd;
    }

    public void setSolDateEnd(int solDateEnd) {
        this.solDateEnd = solDateEnd;
    }

    public List<String> getCameras() {
        return cameras;
    }

    public void setCameras(List<String> cameras) {
        this.cameras = cameras;
    }

    @Override
    public String toString() {
        return "ImageSearch{" +
                "roverName='" + roverName + '\'' +
                ", earthDateStart='" + earthDateStart + '\'' +
                ", earthDateEnd='" + earthDateEnd + '\'' +
                ", solDateStart=" + solDateStart +
                ", solDateEnd=" + solDateEnd +
                ", cameras=" + cameras +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
