package validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VoteRequestValidator.class)
public @interface ValidVoteRequest {
	
    String message() default "Некоректні дані ідентифікаторів запиту";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
