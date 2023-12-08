package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.events.UserRegisteredEvent;

public interface UserActivationService {

    void userRegistered(UserRegisteredEvent userRegisteredEvent);

    void activationLinkCleanUp();

    String createActivationCode(String userEmail);

    void cleanUpObsoleteActivationLinks();
}
