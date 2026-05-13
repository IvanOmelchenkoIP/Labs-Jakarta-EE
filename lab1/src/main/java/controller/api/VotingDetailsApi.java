package controller.api;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import controller.WebConstants;
import dto.ErrorResponse;
import dto.VotingDetailsResponse;
import model.Voting;
import service.VotingService;

import java.util.Optional;
import java.util.Set;

@Path("/voting/detail")
@Produces(MediaType.APPLICATION_JSON)
public class VotingDetailsApi {

	@Context
	private ServletContext servletContext;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Path("/{id}")
	public Response getVotingDetail(@PathParam("id") Long id) {
		if (id == null || id <= 0) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Голосування не знайдено"))
					.build();
		}

		VotingService service = (VotingService) servletContext.getAttribute(WebConstants.ATTR_VOTING_SERVICE);
		Optional<Voting> voting = service.findById(id);

		if (voting.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Голосування не знайдено"))
					.build();
		}

		boolean hasVoted = hasSessionVoted(servletRequest.getSession(true), id);
		VotingDetailsResponse response = new VotingDetailsResponse(voting.get(), hasVoted);
		return Response.ok(response).build();

	}

	private boolean hasSessionVoted(HttpSession session, long votingId) {
		@SuppressWarnings("unchecked")
		Set<Long> voted = (Set<Long>) session.getAttribute(WebConstants.SESSION_VOTED_IDS);
		return voted != null && voted.contains(votingId);
	}

}