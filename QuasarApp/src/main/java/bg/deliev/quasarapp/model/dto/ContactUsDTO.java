package bg.deliev.quasarapp.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactUsDTO {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Please use a legitimate email address")
    private String email;

    @NotEmpty(message = "Message is required.")
    private String message;
}
