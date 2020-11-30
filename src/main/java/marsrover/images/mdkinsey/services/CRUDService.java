package marsrover.images.mdkinsey.services;

import java.util.List;

public interface CRUDService<T>  {
    List<?> listAll();

    T getById(Integer id);

    T saveOrUpdate(T domainObject);

}
