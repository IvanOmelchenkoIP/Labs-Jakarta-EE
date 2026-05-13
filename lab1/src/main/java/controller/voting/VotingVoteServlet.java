package controller.voting;

import java.io.IOException;

import controller.WebConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.VoteResult;
import service.VotingService;

@WebServlet("/voting/vote")
public class VotingVoteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long votingId = parseLong(request.getParameter("votingId"));
        long candidateId = parseLong(request.getParameter("candidateId"));
        VotingService service = (VotingService) getServletContext().getAttribute(WebConstants.ATTR_VOTING_SERVICE);

        HttpSession session = request.getSession(true);
        
        Long userId = (Long) session.getAttribute("SESSION_USER_ID");
        if (userId == null) {
            userId = Math.abs(java.util.UUID.randomUUID().getMostSignificantBits());
            session.setAttribute("SESSION_USER_ID", userId);
        }

        if (VotingDetailServlet.hasSessionVoted(session, votingId)) {
            redirectWithNotice(request, response, votingId, "already");
            return;
        }

        VoteResult result = service.castVote(votingId, candidateId, userId);
        
        switch (result) {
            case OK:
                VotingDetailServlet.markSessionVoted(session, votingId);
                redirectWithNotice(request, response, votingId, "ok");
                break;
            case NOT_FOUND:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
            case NOT_ACTIVE:
                redirectWithNotice(request, response, votingId, "inactive");
                break;
            case INVALID_CANDIDATE:
                redirectWithNotice(request, response, votingId, "badcandidate");
                break;
            case ALREADY_VOTED: 
                redirectWithNotice(request, response, votingId, "already");
                break;
            default:
                redirectWithNotice(request, response, votingId, "error");
        }
    }

    private void redirectWithNotice(HttpServletRequest request, HttpServletResponse response, long votingId,
            String notice) throws IOException {
        String ctx = request.getContextPath();
        response.sendRedirect(response.encodeRedirectURL(ctx + "/voting/detail?id=" + votingId + "&notice=" + notice));
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