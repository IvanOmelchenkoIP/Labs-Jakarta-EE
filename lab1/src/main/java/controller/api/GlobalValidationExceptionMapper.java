package controller.api;

import dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		Map<String, String> details = new HashMap<>();

		exception.getConstraintViolations().forEach(violation -> {
			String propertyPath = violation.getPropertyPath().toString();
			String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);

			details.put(fieldName, violation.getMessage());
		});

		ErrorResponse errorResponse = new ErrorResponse("Помилка валідації вхідних даних", details);

		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(errorResponse)
				.build();
	}
}
