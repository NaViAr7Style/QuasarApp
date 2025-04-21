package bg.deliev.quasarapp.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchesPatternValidator implements ConstraintValidator<MatchesPattern, String> {

    private String regex;

    @Override
    public void initialize(MatchesPattern constraintAnnotation) {
        this.regex = constraintAnnotation.regex();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}
