package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.entity.UserActivationCodeEntity;
import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.events.UserRegisteredEvent;
import bg.deliev.quasarapp.repository.UserActivationCodeRepository;
import bg.deliev.quasarapp.repository.UserRepository;
import bg.deliev.quasarapp.service.interfaces.EmailService;
import bg.deliev.quasarapp.service.interfaces.UserActivationService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class UserActivationServiceImpl implements UserActivationService {

    private static final String ACTIVATION_CODE_SYMBOLS =   "abcdefghijklmnopqrstuvwxyz" +
                                                            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                                            "0123456789";

    private static final int ACTIVATION_CODE_LENGTH = 20;

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;

    public UserActivationServiceImpl(EmailService emailService,
                                     UserRepository userRepository,
                                     UserActivationCodeRepository userActivationCodeRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
    }

    @Override
    @EventListener(UserRegisteredEvent.class)
    public void userRegistered(UserRegisteredEvent event) {
        String activationCode = createActivationCode(event.getUserEmail());

        emailService.sendRegistrationEmail(event.getUserEmail(), event.getUserFullName(), activationCode);
    }

    @Override
    public void activationLinkCleanUp() {
        Instant cutOffTime = Instant.now().minusSeconds(24 * 60 * 60);
        List<UserActivationCodeEntity> oldActivationCodes = userActivationCodeRepository
                .findAllByCreatedBefore(cutOffTime);

        userActivationCodeRepository.deleteAll(oldActivationCodes);
    }

    @Override
    public String createActivationCode(String userEmail) {
        UserActivationCodeEntity userActivationCodeEntity = new UserActivationCodeEntity();

        UserEntity user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found!"));

        userActivationCodeEntity.setActivationCode(generateActivationCode());
        userActivationCodeEntity.setCreated(Instant.now());
        userActivationCodeEntity.setUser(user);

        userActivationCodeRepository.save(userActivationCodeEntity);

        return userActivationCodeEntity.getActivationCode();
    }

    private static String generateActivationCode() {

        StringBuilder activationCode = new StringBuilder();
        Random random = new SecureRandom();

        for (int i = 0; i < ACTIVATION_CODE_LENGTH; i++) {
            int randInt = random.nextInt(ACTIVATION_CODE_SYMBOLS.length());
            activationCode.append(ACTIVATION_CODE_SYMBOLS.charAt(randInt));
        }

        return activationCode.toString();
    }
}
