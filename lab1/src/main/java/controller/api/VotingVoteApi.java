package controller.api;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import dto.ErrorResponse;
import dto.MessageResponse;
import dto.VoteRequest;
import service.VoteResult;
import service.VotingService;

@Path("/voting/votes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VotingVoteApi {

	@EJB
	private VotingService service;

	@POST
	public Response castVote(@HeaderParam("X-User-Id") Long userId, @Valid VoteRequest dto) {
		if (userId == null) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(new ErrorResponse("Дія неможлива для неавторизованого користувача")).build();
		}

		long votingId = dto.getVotingId();
		long candidateId = dto.getCandidateId();

		VoteResult result = service.castVote(votingId, candidateId, userId);

		switch (result) {
		case OK:
			return Response.ok(new MessageResponse("Голос успішно зараховано")).build();

		case ALREADY_VOTED:
			return Response.status(Response.Status.CONFLICT)
					.entity(new ErrorResponse("Ви вже віддали свій голос в цьому голосуванні")).build();

		case NOT_FOUND:
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Голосування не знайдено"))
					.build();

		case NOT_ACTIVE:
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("Голосування наразі не активне")).build();

		case INVALID_CANDIDATE:
			return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Обраного кандидата не існує"))
					.build();

		default:
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("Помилка сервера при обробці голосу")).build();
		}
	}
}
