package bg.deliev.quasarapp.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchesPatternValidator.class)
public @interface MatchesPattern {

    String regex();

    String message() default "Must contain at least 1 uppercase, 1 lowercase and 1 special character.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
