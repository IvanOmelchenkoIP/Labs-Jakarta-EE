package service;

import java.util.List;
import java.util.Optional;

import jakarta.ejb.Local;
import model.Voting;
import model.VotingStatus;

@Local
public interface VotingService {

    List<Voting> findAll();

    Optional<Voting> findById(long id);

    VoteResult castVote(long votingId, long candidateId, long userId);

    Voting createVoting(String title, String description, List<String> candidateNames, long ownerId);

    void updateStatus(long votingId, VotingStatus status);
    
    void deleteVoting(long id);
    
    public boolean hasUserVoted(long votingId, long userId);
}
