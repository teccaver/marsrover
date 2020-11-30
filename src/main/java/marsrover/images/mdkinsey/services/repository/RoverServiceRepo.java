package marsrover.images.mdkinsey.services.repository;

import marsrover.images.mdkinsey.domain.Rover;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoverServiceRepo extends CrudRepository<Rover, String> {

}
