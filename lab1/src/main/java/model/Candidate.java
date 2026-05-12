package model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long id;
    private final String name;
    private final AtomicInteger voteCount;

    public Candidate(long id, String name) {
        this.id = id;
        this.name = name;
        this.voteCount = new AtomicInteger(0);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVoteCount() {
        return voteCount.get();
    }

    public void incrementVotes() {
        voteCount.incrementAndGet();
    }
}
