package bg.deliev.quasarapp.repository;

import bg.deliev.quasarapp.model.entity.ManufacturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<ManufacturerEntity, Long> {
}
