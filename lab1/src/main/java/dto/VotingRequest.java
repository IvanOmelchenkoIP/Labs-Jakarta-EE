package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import validation.MinCandidates;
import java.util.List;

public class VotingRequest {

	@NotBlank(message = "Назва голосування є обов'язковою")
	@Size(max = 128, message = "Назва не повинна перевищувати 128 символів")
	private String title;

	private String description;

	@MinCandidates(value = 2)
	private List<String> candidates;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<String> candidates) {
		this.candidates = candidates;
	}
}
