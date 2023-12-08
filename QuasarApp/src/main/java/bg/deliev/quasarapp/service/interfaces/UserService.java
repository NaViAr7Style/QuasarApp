package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.dto.UpdateUserRolesDTO;
import bg.deliev.quasarapp.model.dto.UserDetailsDTO;
import bg.deliev.quasarapp.model.dto.UserManagementDTO;
import bg.deliev.quasarapp.model.dto.UserRegistrationDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean registerUser(UserRegistrationDTO userRegistrationDTO);

    UserDetailsDTO findByUsername(String username);

    List<UserManagementDTO> getAllUsers();

    Optional<UserManagementDTO> findUserById(Long id);

    void deleteUserById(Long id);

    void updateUserRoles(UpdateUserRolesDTO updateUserRolesDTO);

    void verifyUser(String activationCode);
}
