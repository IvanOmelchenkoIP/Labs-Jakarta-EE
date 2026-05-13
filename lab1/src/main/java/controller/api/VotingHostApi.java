package controller.api;

import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import controller.WebConstants;
import dto.ErrorResponse;
import dto.UpdateStatusRequest;
import dto.VotingRequest;
import model.Voting;
import model.VotingStatus;
import service.VotingService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/voting/host")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VotingHostApi {

	@Context
	private ServletContext servletContext;

	private VotingService getService() {
		return (VotingService) servletContext.getAttribute(WebConstants.ATTR_VOTING_SERVICE);
	}

	@GET
	public Response getAllVotings(@QueryParam("status") VotingStatus statusFilter,
			@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size") @DefaultValue("10") int size) {

		List<Voting> list = new ArrayList<>(getService().findAll());
		list.sort(Comparator.comparingLong(Voting::getId));

		if (statusFilter != null) {
			list = list.stream().filter(v -> v.getStatus() == statusFilter).collect(Collectors.toList());
		}

		int fromIndex = (page - 1) * size;
		if (fromIndex >= list.size()) {
			return Response.ok(new ArrayList<>()).build();
		}
		int toIndex = Math.min(fromIndex + size, list.size());
		List<Voting> paginatedList = list.subList(fromIndex, toIndex);

		return Response.ok(paginatedList).build();
	}

	@POST
	public Response createVoting(@HeaderParam("X-User-Id") Long ownerId, @Valid VotingRequest dto) {
		if (ownerId == null) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(new ErrorResponse("Дія неможлива для неавторизованого користувача")).build();
		}

		Voting voting = getService().createVoting(dto.getTitle(),
				dto.getDescription() != null ? dto.getDescription() : "", dto.getCandidates(), ownerId);
		return Response.status(Status.CREATED).entity(voting).build();
	}

	@PUT
	@Path("/{id}/status")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStatus(@PathParam("id") Long id, @HeaderParam("X-User-Id") Long ownerId,
			@Valid UpdateStatusRequest dto) {

		if (ownerId == null) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(new ErrorResponse("Дія неможлива для неавторизованого користувача")).build();
		}

		Optional<Voting> votingOpt = getService().findById(id);

		if (votingOpt.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Голосування не знайдено"))
					.build();
		}

		Voting voting = votingOpt.get();
		if (!Objects.equals(voting.getOwnerId(), ownerId)) {
			return Response.status(Response.Status.FORBIDDEN).entity(new ErrorResponse("Доступ заборонено")).build();
		}

		String action = dto.getAction();

		if ("start".equalsIgnoreCase(action)) {
			getService().updateStatus(id, VotingStatus.ACTIVE);
		} else if ("stop".equalsIgnoreCase(action)) {
			getService().updateStatus(id, VotingStatus.CLOSED);
		}

		return Response.ok(voting).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteVoting(@PathParam("id") Long id, @HeaderParam("X-User-Id") Long ownerId) {
		if (ownerId == null) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(new ErrorResponse("Дія неможлива для неавторизованого користувача")).build();
		}

		Optional<Voting> votingOpt = getService().findById(id);

		if (votingOpt.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Голосування не знайдено"))
					.build();
		}

		Voting voting = votingOpt.get();
		if (voting.getOwnerId() != ownerId) {
			return Response.status(Response.Status.FORBIDDEN).entity(new ErrorResponse("Доступ заборонено")).build();
		}

		getService().deleteVoting(id);

		return Response.noContent().build();
	}
}
