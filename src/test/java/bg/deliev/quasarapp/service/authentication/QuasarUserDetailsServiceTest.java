package bg.deliev.quasarapp.service.authentication;

import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static bg.deliev.quasarapp.testUtils.TestUserUtil.*;
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
    void testLoadUserByUsername_UserFound_ReturnsCorrectUserDetails() {
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

    @Test
    void testLoadUserByUsername_UserFound_NoRoles() {
        // Arrange
        UserEntity testUser = createTestUserWithoutRoles();

        when(mockUserRepository.findByEmail(testUser.getEmail()))
            .thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = serviceToTest.loadUserByUsername(testUser.getEmail());

        // Assert
        assertNotNull(userDetails);
        assertEquals(testUser.getEmail(), userDetails.getUsername());
        assertEquals(testUser.getPassword(), userDetails.getPassword());
        assertEquals(0, userDetails.getAuthorities().size(), "Expected no authorities when user has no roles");
    }
}