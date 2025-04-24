package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.event.UserRegisteredEvent;

public interface UserActivationService {

    void userRegistered(UserRegisteredEvent userRegisteredEvent);

    void activationLinkCleanUp();

    String createActivationCode(String userEmail);
}
