package bg.deliev.quasarapp.init;

import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserRolesInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public UserRolesInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        if (roleRepository.count() == 0) {

            List<UserRoleEntity> roles = new ArrayList<>();

            Arrays.stream(UserRoleEnum.values())
                    .forEach(e -> {
                        UserRoleEntity userRoleEntity = new UserRoleEntity();
                        userRoleEntity.setRole(e);
                        roles.add(userRoleEntity);
                    });

            roleRepository.saveAll(roles);
        }

    }

}
