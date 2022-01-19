package ru.itmo.robq.web4.data;

import org.springframework.stereotype.Repository;
import ru.itmo.robq.web4.model.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
@RepositoryRestResource(exported = false)
public interface PointRepository extends CrudRepository<Point, Long> {

    Collection<Point> findAllByUserUid(Long userId);

    @Transactional
    Collection<Point> deleteAllByUserUid(Long userId);
}