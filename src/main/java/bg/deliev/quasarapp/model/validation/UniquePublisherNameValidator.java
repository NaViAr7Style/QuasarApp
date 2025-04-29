package bg.deliev.quasarapp.model.validation;

import bg.deliev.quasarapp.repository.PublisherRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniquePublisherNameValidator implements ConstraintValidator<UniquePublisherName, String> {

    private final PublisherRepository publisherRepository;

    public UniquePublisherNameValidator(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        return publisherRepository
            .findByName(value)
            .isEmpty();
    }
}