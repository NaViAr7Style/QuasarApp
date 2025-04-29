package bg.deliev.quasarapp.service.scheduler;

import bg.deliev.quasarapp.service.interfaces.UserActivationService;
import org.springframework.stereotype.Component;

@Component
public class ActivationLinkCleanUpScheduler {

    private final UserActivationService userActivationService;

    public ActivationLinkCleanUpScheduler(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

//  Unnecessary after the removal of user activation

//    @Scheduled(cron = "0 0 2 * * ?")
//    public void cleanUp() {
//        userActivationService.activationLinkCleanUp();
//    }
}
