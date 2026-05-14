package controller.api;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import dto.ErrorResponse;
import dto.VotingDetailsResponse;
import model.Voting;
import service.VotingService;

import java.util.Optional;

@Path("/voting/detail")
@Produces(MediaType.APPLICATION_JSON)
public class VotingDetailsApi {

	@Context
	private ServletContext servletContext;

	@Context
	private HttpServletRequest servletRequest;

	@EJB
	private VotingService service;

	@GET
	@Path("/{id}")
	public Response getVotingDetail(@PathParam("id") Long id, @HeaderParam("X-User-Id") Long userId) {
		if (id == null || id <= 0) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Голосування не знайдено"))
					.build();
		}

		Optional<Voting> voting = service.findById(id);

		if (voting.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Голосування не знайдено"))
					.build();
		}

		boolean hasVoted = false;
	    if (userId != null) {
	        hasVoted = service.hasUserVoted(id, userId);
	    }

	    VotingDetailsResponse response = new VotingDetailsResponse(voting.get(), hasVoted);
	    return Response.ok(response).build();

	}

}