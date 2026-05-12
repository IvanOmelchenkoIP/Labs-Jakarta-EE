package service;

import java.util.List;

import model.Voting;
import model.VotingStatus;

public interface VotingService {

    List<Voting> findAll();

    Voting findById(long id);

    VoteResult castVote(long votingId, long candidateId);

    Voting createVoting(String title, String description, List<String> candidateNames, long ownerId);

    void updateStatus(long votingId, VotingStatus status);
}
