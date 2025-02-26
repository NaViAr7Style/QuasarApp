package bg.deliev.quasarapp.testUtils;

import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DBInit implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() != 0) {
            return;
        }

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
