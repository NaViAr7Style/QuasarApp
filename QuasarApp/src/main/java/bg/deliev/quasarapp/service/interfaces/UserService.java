package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.dto.UserRegistrationDTO;

public interface UserService {

    boolean registerUser(UserRegistrationDTO userRegistrationDTO);
}
