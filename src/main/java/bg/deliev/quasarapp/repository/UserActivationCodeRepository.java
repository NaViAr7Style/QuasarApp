package bg.deliev.quasarapp.repository;

import bg.deliev.quasarapp.model.entity.UserActivationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserActivationCodeRepository extends JpaRepository<UserActivationCodeEntity, Long> {

    Optional<UserActivationCodeEntity> findByActivationCode(String activationCode);

    List<UserActivationCodeEntity> findAllByUserId(Long id);

    List<UserActivationCodeEntity> findAllByCreatedBefore(Instant cutOffTime);
}
