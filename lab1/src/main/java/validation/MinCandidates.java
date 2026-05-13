package validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CandidatesValidator.class)
public @interface MinCandidates {
	
    String message() default "Голосування повинно мати принаймні {value} кандидатів";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value() default 2;
}
