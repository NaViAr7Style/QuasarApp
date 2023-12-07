package bg.deliev.quasarapp.model.validation;

import bg.deliev.quasarapp.repository.GameRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueGameNameValidator implements ConstraintValidator<UniqueGameName, String> {

    private final GameRepository gameRepository;

    public UniqueGameNameValidator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return gameRepository
                .findByName(value)
                .isEmpty();
    }
}
