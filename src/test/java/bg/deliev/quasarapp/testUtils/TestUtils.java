package bg.deliev.quasarapp.testUtils;

import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TestUtils {

    public static UserEntity createValidUser() {
        UserEntity user = new UserEntity();

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setRole(UserRoleEnum.USER);

        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("test_email@test.com");
        user.setPassword("test");
        user.setActive(true);
        user.setRoles(Set.of(userRoleEntity));

        return user;
    }

    public static UserEntity createValidAdmin() {
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

    public static UserEntity createValidUserWithoutRoles() {
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

    public static GameEntity createValidGame(PublisherEntity publisher) {
        GameEntity game = new GameEntity();

        game.setName("Test Game");
        game.setDescription("Test Description");
        game.setPrice(new BigDecimal("19.99"));
        game.setGenre(GameGenreEnum.ACTION);
        game.setThumbnailUrl("https://example.com/thumbnail.jpg");
        game.setPublisher(publisher);

        return game;
    }

    public static PublisherEntity createValidPublisher() {
        PublisherEntity publisher = new PublisherEntity();

        publisher.setName("Test Publisher");
        publisher.setThumbnailUrl("https://example.com/publisher.jpg");

        return publisher;
    }
}
