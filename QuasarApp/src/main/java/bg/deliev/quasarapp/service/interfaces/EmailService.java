package bg.deliev.quasarapp.service.interfaces;

public interface EmailService {

    void sendRegistrationEmail(String userEmail, String userFullName, String activationCode);
}
