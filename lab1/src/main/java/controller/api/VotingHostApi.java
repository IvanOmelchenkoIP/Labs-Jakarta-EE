package controller.api;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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

    @EJB
    private VotingService service;

    @GET
    public Response getAllVotings(
            @QueryParam("status") VotingStatus statusFilter,
            @QueryParam("page") @DefaultValue("1") int page, 
            @QueryParam("size") @DefaultValue("10") int size) {

        int validatedPage = Math.max(1, page);
        int validatedSize = Math.max(1, size);

        List<Voting> list = new ArrayList<>(service.findAll());
        list.sort(Comparator.comparingLong(Voting::getId));

        if (statusFilter != null) {
            list = list.stream()
                    .filter(v -> v.getStatus() == statusFilter)
                    .collect(Collectors.toList());
        }

        int fromIndex = (validatedPage - 1) * validatedSize;
        if (fromIndex >= list.size()) {
            return Response.ok(new ArrayList<>()).build();
        }
        
        int toIndex = Math.min(fromIndex + validatedSize, list.size());
        List<Voting> paginatedList = list.subList(fromIndex, toIndex);

        return Response.ok(paginatedList).build();
    }

    @POST
    public Response createVoting(@HeaderParam("X-User-Id") Long ownerId, @Valid VotingRequest dto) {
        if (ownerId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Дія неможлива для неавторизованого користувача"))
                    .build();
        }

        Voting voting = service.createVoting(
                dto.getTitle(),
                dto.getDescription() != null ? dto.getDescription() : "", 
                dto.getCandidates(), 
                ownerId
        );
        return Response.status(Status.CREATED).entity(voting).build();
    }

    @PUT
    @Path("/{id}/status")
    public Response updateStatus(
            @PathParam("id") Long id, 
            @HeaderParam("X-User-Id") Long ownerId,
            @Valid UpdateStatusRequest dto) {

        if (ownerId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Дія неможлива для неавторизованого користувача"))
                    .build();
        }

        Optional<Voting> votingOpt = service.findById(id);

        if (votingOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Голосування не знайдено"))
                    .build();
        }

        Voting voting = votingOpt.get();
        if (!Objects.equals(voting.getOwnerId(), ownerId)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse("Доступ заборонено"))
                    .build();
        }

        String action = dto.getAction();
        VotingStatus newStatus = null;

        if ("start".equalsIgnoreCase(action)) {
            newStatus = VotingStatus.ACTIVE;
        } else if ("stop".equalsIgnoreCase(action)) {
            newStatus = VotingStatus.CLOSED;
        }

        if (newStatus != null) {
            service.updateStatus(id, newStatus);
            voting.setStatus(newStatus);
        }

        return Response.ok(voting).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteVoting(@PathParam("id") Long id, @HeaderParam("X-User-Id") Long ownerId) {
        if (ownerId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Дія неможлива для неавторизованого користувача"))
                    .build();
        }

        Optional<Voting> votingOpt = service.findById(id);

        if (votingOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Голосування не знайдено"))
                    .build();
        }

        Voting voting = votingOpt.get();
        if (!Objects.equals(voting.getOwnerId(), ownerId)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse("Доступ заборонено"))
                    .build();
        }

        service.deleteVoting(id);
        return Response.noContent().build();
    }
}
