package controller.voting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controller.WebConstants;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Voting;
import model.VotingStatus;
import service.VotingService;

@WebServlet("/voting/host")
public class VotingHostServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
    private VotingService service;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Voting> sorted = new ArrayList<>(service.findAll());
		sorted.sort(Comparator.comparingLong(Voting::getId));
		request.setAttribute("votings", sorted);
		request.setAttribute("demoOwnerId", WebConstants.DEMO_OWNER_ID);
		request.setAttribute("flashNotice", request.getParameter("notice"));
		request.getRequestDispatcher(WebConstants.VIEW_HOST).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String action = request.getParameter("action");
		String ctx = request.getContextPath();

		if ("create".equals(action)) {
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String candidatesRaw = request.getParameter("candidates");
			List<String> names = splitCandidateNames(candidatesRaw);
			if (title != null && !title.isBlank() && names.size() >= 2) {
				service.createVoting(title, description != null ? description : "", names, WebConstants.DEMO_OWNER_ID);
				response.sendRedirect(response.encodeRedirectURL(ctx + "/voting/host?notice=created"));
			} else {
				response.sendRedirect(response.encodeRedirectURL(ctx + "/voting/host?notice=createbad"));
			}
			return;
		}

		long votingId = parseLong(request.getParameter("votingId"));
		Optional<Voting> votingOptional = service.findById(votingId);
		if (votingOptional.isEmpty()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Голосування не знайдено");
			return;
		}

		Voting voting = votingOptional.get();

		if (voting.getOwnerId() != WebConstants.DEMO_OWNER_ID) {
			response.sendRedirect(response.encodeRedirectURL(ctx + "/voting/host?notice=forbidden"));
			return;
		}

		if ("start".equals(action)) {
			service.updateStatus(votingId, VotingStatus.ACTIVE);
			response.sendRedirect(response.encodeRedirectURL(ctx + "/voting/host?notice=started"));
		} else if ("stop".equals(action)) {
			service.updateStatus(votingId, VotingStatus.CLOSED);
			response.sendRedirect(response.encodeRedirectURL(ctx + "/voting/host?notice=stopped"));
		} else {
			response.sendRedirect(response.encodeRedirectURL(ctx + "/voting/host?notice=unknown"));
		}
	}

	private static List<String> splitCandidateNames(String raw) {
		if (raw == null || raw.isBlank()) {
			return List.of();
		}
		return Arrays.stream(raw.split("[\\n,;]+")).map(String::trim).filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
	}

	private static long parseLong(String s) {
		if (s == null || s.isBlank()) {
			return -1L;
		}
		try {
			return Long.parseLong(s.trim());
		} catch (NumberFormatException e) {
			return -1L;
		}
	}
}
