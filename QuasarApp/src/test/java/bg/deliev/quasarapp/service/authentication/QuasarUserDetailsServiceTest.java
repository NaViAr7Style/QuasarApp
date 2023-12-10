package bg.deliev.quasarapp.service.authentication;

import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuasarUserDetailsServiceTest {

    private QuasarUserDetailsService serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new QuasarUserDetailsService(mockUserRepository);
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {

        assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("test_email@test.com")
        );
    }

    @Test
    void testLoadUserByUsernameUserFoundException() {
        // Arrange
        UserEntity testUser = createTestUser();

        when(mockUserRepository.findByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = serviceToTest.loadUserByUsername(testUser.getEmail());

        // Assert
        assertNotNull(userDetails);
        assertEquals(testUser.getEmail(), userDetails.getUsername(), "Username is not mapped to email.");
        assertEquals(testUser.getPassword(), userDetails.getPassword());
        assertEquals(2, userDetails.getAuthorities().size());

        assertTrue(
                containsAuthority(
                        userDetails,
                        "ROLE_" + UserRoleEnum.ADMIN
                ),
                "The user is not admin"
        );

        assertTrue(
                containsAuthority(
                        userDetails,
                        "ROLE_" + UserRoleEnum.USER
                ),
                "The user doesn't have 'user' role"
        );

    }

    private boolean containsAuthority(UserDetails userDetails, String expectedAuthority) {
        return userDetails
                .getAuthorities()
                .stream()
                .anyMatch(authority -> expectedAuthority.equals(authority.getAuthority()));
    }

    private static UserEntity createTestUser() {
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
        user.setActive(false);
        user.setRoles(roles);

        return user;
    }

}