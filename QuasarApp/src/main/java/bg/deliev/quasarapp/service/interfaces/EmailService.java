package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.dto.ContactUsDTO;

public interface EmailService {

    void sendRegistrationEmail(String userEmail, String userFullName, String activationCode);

    void sendFeedbackEmail(ContactUsDTO contactUsDTO);
}
