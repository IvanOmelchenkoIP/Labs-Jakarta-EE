package dto;

import model.Voting;

public class VotingDetailsResponse {
	
	private Voting voting;
	private boolean hasVoted;

	public VotingDetailsResponse(Voting voting, boolean hasVoted) {
		this.voting = voting;
		this.hasVoted = hasVoted;
	}

	public Voting getVoting() {
		return voting;
	}

	public void setVoting(Voting voting) {
		this.voting = voting;
	}

	public boolean isHasVoted() {
		return hasVoted;
	}

	public void setHasVoted(boolean hasVoted) {
		this.hasVoted = hasVoted;
	}
}
