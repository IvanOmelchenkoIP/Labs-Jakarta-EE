package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateStatusRequest {

	@NotBlank(message = "Дія не може бути порожньою")
	@Pattern(regexp = "^(?i)(start|stop)$", message = "Дозволені дії: start або stop")
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
