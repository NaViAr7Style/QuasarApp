package bg.deliev.quasarapp.model.dto;

import bg.deliev.quasarapp.model.validation.MatchesPattern;
import bg.deliev.quasarapp.model.validation.StringFieldMatch;
import bg.deliev.quasarapp.model.validation.UniqueUserEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@StringFieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords should match."
)
@Getter
@Setter
public class UserRegistrationDTO {

    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Please use a legitimate email address")
    @UniqueUserEmail(message = "Empty email or already in use")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    @MatchesPattern(
            regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must contain at least 1 uppercase, 1 lowercase and 1 special character."
    )
    private String password;

    @NotEmpty(message = "Confirming password is required")
    private String confirmPassword;
}
