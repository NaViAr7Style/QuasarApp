package bg.deliev.quasarapp.testUtils;

import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TestUserUtil {

    public static UserEntity createTestUser() {
        UserEntity user = new UserEntity();

        Set<UserRoleEntity> roles = Arrays.stream(UserRoleEnum.values())
            .map(userRoleEnum -> {
                UserRoleEntity userRoleEntity = new UserRoleEntity();
                userRoleEntity.setRole(userRoleEnum);
                return userRoleEntity;
            }).collect(Collectors.toSet());

        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("test_email@test.com");
        user.setPassword("test");
        user.setActive(true);
        user.setRoles(roles);

        return user;
    }

    public static UserEntity createTestUserWithoutRoles() {
        UserEntity user = new UserEntity();

        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("test_email_no_roles@test.com");
        user.setPassword("test");
        user.setActive(true);
        user.setRoles(Set.of());

        return user;
    }

    public static boolean containsAuthority(UserDetails userDetails, String expectedAuthority) {
        return userDetails
            .getAuthorities()
            .stream()
            .anyMatch(authority -> expectedAuthority.equals(authority.getAuthority()));
    }
}
