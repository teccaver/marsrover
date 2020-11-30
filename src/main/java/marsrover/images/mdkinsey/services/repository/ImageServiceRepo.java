package marsrover.images.mdkinsey.services.repository;

import marsrover.images.mdkinsey.domain.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageServiceRepo extends CrudRepository<Image, Integer> {
    Image findImageByPhotoId(int photoId);
    List<Image> findImageBySolDateLessThan(int maxSolDate);
    List<Image> findImageBySolDateGreaterThanAndSolDateLessThanAndRoverNameAndCameraName(int minSolData, int maxSolDate, String roverName, String cameraName);
    List<Image> findImageBySolDateAndRoverNameAndCameraName(int minSolData, String roverName, String cameraName);

}
