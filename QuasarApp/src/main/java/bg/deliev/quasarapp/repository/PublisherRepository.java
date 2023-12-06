package bg.deliev.quasarapp.repository;

import bg.deliev.quasarapp.model.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {
}
