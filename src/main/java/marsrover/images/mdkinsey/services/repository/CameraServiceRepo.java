package marsrover.images.mdkinsey.services.repository;

import marsrover.images.mdkinsey.domain.Camera;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraServiceRepo extends CrudRepository<Camera, Integer> {
    //    public Camera saveOrUpdate(Camera camera, javax.persistence.EntityManager em);
    public Camera findCameraByCameraNameAndRover(String cameraName, String roverName);
}
