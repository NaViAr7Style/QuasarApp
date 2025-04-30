package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.ContactUsDTO;
import bg.deliev.quasarapp.service.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String quasarEmail;

    public EmailServiceImpl(TemplateEngine templateEngine,
                            JavaMailSender javaMailSender,
                            @Value("${mail.quasar}") String quasarEmail) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.quasarEmail = quasarEmail;
    }

    @Override
    public void sendRegistrationEmail(String userEmail, String userFullName, String activationCode) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(quasarEmail);
            mimeMessageHelper.setReplyTo(quasarEmail);
            mimeMessageHelper.setSubject("Welcome to Quasar!");
            mimeMessageHelper.setText(generateRegistrationEmailBody(userFullName, activationCode), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendFeedbackEmail(ContactUsDTO contactUsDTO) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            String senderName = contactUsDTO.getName();

            mimeMessageHelper.setTo(quasarEmail);
            mimeMessageHelper.setFrom(senderName);
            mimeMessageHelper.setReplyTo(senderName);
            mimeMessageHelper.setSubject("Contact us form - feedback");
            mimeMessageHelper.setText(
                    generateContactUsEmailBody(contactUsDTO.getName(), contactUsDTO.getMessage()),
                    true
            );

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateContactUsEmailBody(String userFullName,String message) {

        Context context = new Context();

        context.setVariable("userFullName", userFullName);
        context.setVariable("message", message);

        return templateEngine.process("/email/contact-us-email", context);
    }


    private String generateRegistrationEmailBody(String userFullName, String activationCode) {

        Context context = new Context();

        context.setVariable("userFullName", userFullName);
        context.setVariable("activation_code", activationCode);

        return templateEngine.process("email/registration-email", context);
    }
}