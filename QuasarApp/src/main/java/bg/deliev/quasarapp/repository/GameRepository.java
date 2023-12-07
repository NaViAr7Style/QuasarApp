package bg.deliev.quasarapp.repository;

import bg.deliev.quasarapp.model.entity.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    Page<GameEntity> findAllByPublisherId(long id, Pageable pageable);

    List<GameEntity> findAllByPublisherId(long id);

    Optional<GameEntity> findByName(String name);
}
