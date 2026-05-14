package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import model.Candidate;
import model.Voting;
import model.VotingStatus;

@Singleton
@Startup
public class InMemoryVotingService implements VotingService {

	private final Map<Long, Voting> byId = new ConcurrentHashMap<>();
	private final Map<Long, Set<Long>> votedUserIdsByVotingId = new ConcurrentHashMap<>();
	private final AtomicLong idSeq = new AtomicLong(100);

	public InMemoryVotingService() {
	}

	@PostConstruct
	private void seedDemoData() {
		long id1 = 1L;
		Voting v1 = new Voting(id1, "Обрання представника бригади",
				"Демонстрація захисту від XSS: <script>alert('xss')</script>", VotingStatus.ACTIVE, 101L);
		addCandidates(v1, List.of("Чирков Максим", "Креславський Михайло", "Кущ Артем"));
		byId.put(id1, v1);

		long id2 = 2L;
		Voting v2 = new Voting(id2, "Обираємо тему доповіді", "Чернетка — ще не запущено.", VotingStatus.DRAFT, 101L);
		addCandidates(v2, List.of("Варіант А", "Варіант Б"));
		byId.put(id2, v2);

		long id3 = 3L;
		Voting v3 = new Voting(id3, "Закрите голосування (архів)", "Результати зафіксовані.", VotingStatus.CLOSED,
				102L);
		addCandidates(v3, List.of("Так", "Ні", "Утримався"));

		findCandidateMutably(v3, v3.getCandidates().get(0).getId()).ifPresent(Candidate::incrementVotes);
		findCandidateMutably(v3, v3.getCandidates().get(0).getId()).ifPresent(Candidate::incrementVotes);

		byId.put(id3, v3);
	}

	private void addCandidates(Voting voting, List<String> names) {
		long base = voting.getId() * 1000;
		int i = 0;
		for (String name : names) {
			Candidate c = new Candidate(base + (++i), name);
			voting.addCandidate(c);
		}
	}

	private Optional<Candidate> findCandidateMutably(Voting voting, long candidateId) {
		for (Candidate c : voting.getCandidates()) {
			if (c.getId() == candidateId) {
				return Optional.of(c);
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean hasUserVoted(long votingId, long userId) {
		Set<Long> users = votedUserIdsByVotingId.get(votingId);
		return users != null && users.contains(userId);
	}

	@Override
	public List<Voting> findAll() {
		return List.copyOf(byId.values());
	}

	@Override
	public Optional<Voting> findById(long id) {
		return Optional.ofNullable(byId.get(id));
	}

	@Override
	public VoteResult castVote(long votingId, long candidateId, long userId) {
		Voting voting = byId.get(votingId);
		if (voting == null) {
			return VoteResult.NOT_FOUND;
		}

		synchronized (voting) {
			if (voting.getStatus() != VotingStatus.ACTIVE) {
				return VoteResult.NOT_ACTIVE;
			}

			if (hasUserVoted(votingId, userId)) {
				return VoteResult.ALREADY_VOTED;
			}

			Optional<Candidate> candidateOpt = findCandidateMutably(voting, candidateId);
			if (candidateOpt.isEmpty()) {
				return VoteResult.INVALID_CANDIDATE;
			}

			votedUserIdsByVotingId.computeIfAbsent(votingId, k -> ConcurrentHashMap.newKeySet()).add(userId);

			Candidate candidate = candidateOpt.get();
			synchronized (candidate) {
				candidate.incrementVotes();
			}
			return VoteResult.OK;
		}
	}

	@Override
	public Voting createVoting(String title, String description, List<String> candidateNames, long ownerId) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(candidateNames, "candidateNames");

		long id = idSeq.getAndIncrement();
		Voting voting = new Voting(id, title.trim(), description != null ? description.trim() : "", VotingStatus.DRAFT,
				ownerId);
		addCandidates(voting, new ArrayList<>(candidateNames));

		byId.put(id, voting);
		return voting;
	}

	@Override
	public void updateStatus(long votingId, VotingStatus status) {
		Voting voting = byId.get(votingId);
		if (voting != null) {
			synchronized (voting) {
				voting.setStatus(status);
			}
		}
	}

	@Override
	public void deleteVoting(long id) {
		byId.remove(id);
	}
}
