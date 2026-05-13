package dto;

import validation.ValidVoteRequest;

@ValidVoteRequest 
public class VoteRequest {
	
	private long votingId;
	private long candidateId;

	public long getVotingId() {
		return votingId;
	}

	public void setVotingId(long votingId) {
		this.votingId = votingId;
	}

	public long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}
}
