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

    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john_doe@test.com";
    private static final String TEST_PASSWORD = "Strong@1234";
    private static final String TEST_THUMBNAIL_URL = "https://example.com/image.jpg";
    private static final String TEST_PUBLISHER_NAME = "Test Publisher";
    private static final String TEST_GAME_NAME = "Test Game";
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(20.00);
    private static final GameGenreEnum TEST_GAME_GENRE = GameGenreEnum.ACTION;
    private static final UserRoleEnum TEST_USER_ROLE = UserRoleEnum.USER;

    public static UserEntity createValidUser() {
        UserEntity user = new UserEntity();

        user.setFirstName(TEST_FIRST_NAME);
        user.setLastName(TEST_LAST_NAME);
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        user.setActive(true);
        user.setRoles(Set.of(new UserRoleEntity(TEST_USER_ROLE)));

        return user;
    }

    public static UserEntity createValidUserWithoutRoles() {
        UserEntity user = new UserEntity();

        user.setFirstName(TEST_FIRST_NAME);
        user.setLastName(TEST_LAST_NAME);
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        user.setActive(true);
        user.setRoles(Set.of());

        return user;
    }

    public static UserRegistrationDTO createValidUserRegistrationDTO() {
        UserRegistrationDTO dto = new UserRegistrationDTO();

        dto.setFirstName(TEST_FIRST_NAME);
        dto.setLastName(TEST_LAST_NAME);
        dto.setEmail(TEST_EMAIL);
        dto.setPassword(TEST_PASSWORD);
        dto.setConfirmPassword(TEST_PASSWORD);

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
        publisher.setThumbnailUrl(TEST_THUMBNAIL_URL);

        return publisher;
    }

    public static AddPublisherDTO createValidAddPublisherDTO(String name) {
        AddPublisherDTO dto = new AddPublisherDTO();

        dto.setName(name);
        dto.setThumbnailUrl(TEST_THUMBNAIL_URL);

        return dto;
    }

    public static GameEntity createValidGame(PublisherEntity publisher) {
        GameEntity game = new GameEntity();

        game.setName(TEST_GAME_NAME);
        game.setDescription(TEST_DESCRIPTION);
        game.setPrice(TEST_PRICE);
        game.setGenre(TEST_GAME_GENRE);
        game.setThumbnailUrl(TEST_THUMBNAIL_URL);
        game.setPublisher(publisher);

        return game;
    }

    public static AddGameDTO createValidAddGameDTO() {
        AddGameDTO dto = new AddGameDTO();

        dto.setName(TEST_GAME_NAME);
        dto.setDescription(TEST_DESCRIPTION);
        dto.setPrice(TEST_PRICE);
        dto.setGenre(TEST_GAME_GENRE);
        dto.setThumbnailUrl(TEST_THUMBNAIL_URL);
        dto.setPublisherName(TEST_PUBLISHER_NAME);

        return dto;
    }

    public static AddGameDTO createValidAddGameDTO(String gameName, String publisherName) {
        AddGameDTO dto = new AddGameDTO();

        dto.setName(gameName);
        dto.setDescription(TEST_DESCRIPTION);
        dto.setPrice(TEST_PRICE);
        dto.setGenre(TEST_GAME_GENRE);
        dto.setThumbnailUrl(TEST_THUMBNAIL_URL);
        dto.setPublisherName(publisherName);

        return dto;
    }
}
