package bg.deliev.quasarapp.repository;

import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<UserRoleEntity, Long> {

    UserRoleEntity getByRole(UserRoleEnum role);

    Set<UserRoleEntity> findAllByRoleIn(Set<UserRoleEnum> roles);
}
