package bg.deliev.quasarapp.testUtils;

import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.RoleRepository;
import bg.deliev.quasarapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserTestDataUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserEntity createTestUser(String username) throws Exception {
        return createUser(Set.of(UserRoleEnum.USER), username);
    }

    public UserEntity createTestAdmin(String username) throws Exception {
        return createUser(Set.of(UserRoleEnum.USER, UserRoleEnum.ADMIN), username);
    }

    public void cleanUp() {
        userRepository.deleteAll();
    }

    private UserEntity createUser(Set<UserRoleEnum> roles, String username) throws Exception {

        Set<UserRoleEntity> roleEntities = roleRepository.findAllByRoleIn(roles);

        UserEntity newUser = new UserEntity();

        newUser.setFirstName("FirstName");
        newUser.setLastName("LastName");
        newUser.setEmail(username);
        newUser.setPassword("test");
        newUser.setActive(true);
        newUser.setRoles(roleEntities);

        return userRepository.save(newUser);
    }

}
