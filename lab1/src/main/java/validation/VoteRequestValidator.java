package validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import dto.VoteRequest;

public class VoteRequestValidator implements ConstraintValidator<ValidVoteRequest, VoteRequest> {

	public VoteRequestValidator() {
	}

	@Override
	public boolean isValid(VoteRequest value, ConstraintValidatorContext context) {
		if (value == null)
			return false;
		return value.getVotingId() > 0 && value.getCandidateId() > 0;
	}
}
