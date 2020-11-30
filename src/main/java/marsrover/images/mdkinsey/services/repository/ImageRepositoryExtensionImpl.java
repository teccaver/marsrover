package marsrover.images.mdkinsey.services.repository;

import marsrover.images.mdkinsey.domain.Camera;
import marsrover.images.mdkinsey.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class ImageRepositoryExtensionImpl implements ImageRepositoryExtension{

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    CameraServiceRepo cameraServiceRepo;


    @Override
    public Image insertImage(Image image) {
        Camera camera = (Camera) cameraServiceRepo.findCameraByCameraNameAndRover(image.getCameraName(),
                                                                                  image.getRoverName());


        return null;
    }
}
