package controller.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import dto.PagedResponse;
import model.Voting;
import model.VotingStatus;
import service.VotingService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/voting")
@Produces(MediaType.APPLICATION_JSON)
public class VotingListApi {

	@EJB
	private VotingService service;

	@GET
	public Response getVotings(@QueryParam("title") String titleFilter, @QueryParam("status") VotingStatus statusFilter,
			@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size") @DefaultValue("10") int size) {

		int validatedPage = Math.max(1, page);
		int validatedSize = Math.max(1, size);

		List<Voting> list = new ArrayList<>(service.findAll());

		list.sort(Comparator.comparingLong(Voting::getId));

		if (statusFilter != null) {
			list = list.stream().filter(v -> v.getStatus() == statusFilter).collect(Collectors.toList());
		}

		if (titleFilter != null && !titleFilter.isBlank()) {
			String lowerFilter = titleFilter.toLowerCase().trim();
			list = list.stream().filter(v -> v.getTitle() != null && v.getTitle().toLowerCase().contains(lowerFilter))
					.collect(Collectors.toList());
		}

		int totalItems = list.size();
		int fromIndex = (validatedPage - 1) * validatedSize;

		if (fromIndex >= totalItems) {
			PagedResponse<Voting> emptyResponse = new PagedResponse<>(new ArrayList<>(), totalItems, validatedPage,
					validatedSize);
			return Response.ok(emptyResponse).build();
		}

		int toIndex = Math.min(fromIndex + validatedSize, totalItems);
		List<Voting> paginatedList = list.subList(fromIndex, toIndex);

		PagedResponse<Voting> pagedResponse = new PagedResponse<>(paginatedList, totalItems, validatedPage,
				validatedSize);
		return Response.ok(pagedResponse).build();
	}
}
