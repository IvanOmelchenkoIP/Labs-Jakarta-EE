package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Voting implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long id;
    private final String title;
    private final String description;
    private VotingStatus status;
    private final long ownerId;
    private final List<Candidate> candidates;

    public Voting(long id, String title, String description, VotingStatus status, long ownerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.candidates = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public VotingStatus getStatus() {
        return status;
    }

    public void setStatus(VotingStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return status == VotingStatus.ACTIVE;
    }

    public boolean isDraft() {
        return status == VotingStatus.DRAFT;
    }

    public boolean isClosed() {
        return status == VotingStatus.CLOSED;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public List<Candidate> getCandidates() {
        return Collections.unmodifiableList(candidates);
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }
}
