package validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class CandidatesValidator implements ConstraintValidator<MinCandidates, List<String>> {
	
    private int min;

    @Override
    public void initialize(MinCandidates constraintAnnotation) {
        this.min = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        return value != null && value.size() >= min;
    }
}
