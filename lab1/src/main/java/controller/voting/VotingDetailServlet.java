package controller.voting;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import controller.WebConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Voting;
import service.VotingService;

@WebServlet("/voting/detail")
public class VotingDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        long id = parseLongOrZero(idParam);
        VotingService service = (VotingService) getServletContext().getAttribute(WebConstants.ATTR_VOTING_SERVICE);
        Voting voting = service.findById(id);
        if (voting == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Голосування не знайдено");
            return;
        }
        HttpSession session = request.getSession(true);
        boolean hasVoted = hasSessionVoted(session, id);
        request.setAttribute("voting", voting);
        request.setAttribute("hasVoted", hasVoted);
        request.getRequestDispatcher(WebConstants.VIEW_DETAIL).forward(request, response);
    }

    static boolean hasSessionVoted(HttpSession session, long votingId) {
        @SuppressWarnings("unchecked")
        Set<Long> voted = (Set<Long>) session.getAttribute(WebConstants.SESSION_VOTED_IDS);
        return voted != null && voted.contains(votingId);
    }

    static void markSessionVoted(HttpSession session, long votingId) {
        @SuppressWarnings("unchecked")
        Set<Long> voted = (Set<Long>) session.getAttribute(WebConstants.SESSION_VOTED_IDS);
        if (voted == null) {
            voted = new HashSet<>();
            session.setAttribute(WebConstants.SESSION_VOTED_IDS, voted);
        }
        voted.add(votingId);
    }

    private static long parseLongOrZero(String s) {
        if (s == null || s.isBlank()) {
            return 0L;
        }
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
