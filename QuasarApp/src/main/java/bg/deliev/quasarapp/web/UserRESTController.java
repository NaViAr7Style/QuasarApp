package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.UpdateUserRolesDTO;
import bg.deliev.quasarapp.model.dto.UserManagementDTO;
import bg.deliev.quasarapp.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRESTController {

    private final UserService userService;

    public UserRESTController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserManagementDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserManagementDTO> findUserById(@PathVariable("id") Long id) {
        Optional<UserManagementDTO> optUserDTO = userService.findUserById(id);

        return optUserDTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserManagementDTO> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping
    public ResponseEntity<UserManagementDTO> updateUserRoles(@RequestBody UpdateUserRolesDTO updateUserRolesDTO) {

        userService.updateUserRoles(updateUserRolesDTO);

        Optional<UserManagementDTO> optUserDTO = userService.findUserById(updateUserRolesDTO.getId());

        return optUserDTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
