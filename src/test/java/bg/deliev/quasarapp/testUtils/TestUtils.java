package bg.deliev.quasarapp.testUtils;

import bg.deliev.quasarapp.model.dto.AddGameDTO;
import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
import bg.deliev.quasarapp.model.dto.UserRegistrationDTO;
import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Set;

public class TestUtils {

    public static UserEntity createValidUser() {
        UserEntity user = new UserEntity();

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setRole(UserRoleEnum.USER);

        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("test_email@test.com");
        user.setPassword("Strong@1234");
        user.setActive(true);
        user.setRoles(Set.of(userRoleEntity));

        return user;
    }

    public static UserEntity createValidUserWithoutRoles() {
        UserEntity user = new UserEntity();

        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("test_email_no_roles@test.com");
        user.setPassword("Strong@1234");
        user.setActive(true);
        user.setRoles(Set.of());

        return user;
    }

    public static UserRegistrationDTO createValidUSerRegistrationDTO() {
        UserRegistrationDTO dto = new UserRegistrationDTO();

        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPassword("Strong@1234");
        dto.setConfirmPassword("Strong@1234");

        return dto;
    }

    public static boolean containsAuthority(UserDetails userDetails, String expectedAuthority) {
        return userDetails
            .getAuthorities()
            .stream()
            .anyMatch(authority -> expectedAuthority.equals(authority.getAuthority()));
    }

    public static PublisherEntity createValidPublisher(String name) {
        PublisherEntity publisher = new PublisherEntity();

        publisher.setName(name);
        publisher.setThumbnailUrl("https://example.com/publisher.jpg");

        return publisher;
    }

    public static AddPublisherDTO createValidAddPublisherDTO(String name) {
        AddPublisherDTO dto = new AddPublisherDTO();

        dto.setName(name);
        dto.setThumbnailUrl("https://example.com/publisher.jpg");

        return dto;
    }

    public static GameEntity createValidGame(PublisherEntity publisher) {
        GameEntity game = new GameEntity();

        game.setName("Test Game");
        game.setDescription("Test Description");
        game.setPrice(BigDecimal.valueOf(20.00));
        game.setGenre(GameGenreEnum.ACTION);
        game.setThumbnailUrl("https://example.com/image.jpg");
        game.setPublisher(publisher);

        return game;
    }

    public static AddGameDTO createValidAddGameDTO() {
        AddGameDTO dto = new AddGameDTO();

        dto.setName("Test Game");
        dto.setDescription("Test Description");
        dto.setPrice(BigDecimal.valueOf(20.00));
        dto.setGenre(GameGenreEnum.ACTION);
        dto.setThumbnailUrl("https://example.com/image.png");
        dto.setPublisherName("SomePublisher");

        return dto;
    }
}
