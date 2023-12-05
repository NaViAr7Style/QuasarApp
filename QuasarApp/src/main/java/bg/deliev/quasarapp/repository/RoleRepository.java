package bg.deliev.quasarapp.repository;

import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<UserRoleEntity, Long> {

    UserRoleEntity getByRole(UserRoleEnum role);
}
