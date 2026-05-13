package controller.api;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import controller.WebConstants;
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

	@Context
	private ServletContext servletContext;

	@GET
	public Response getVotings(@QueryParam("title") String titleFilter, @QueryParam("status") VotingStatus statusFilter,
			@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size") @DefaultValue("10") int size) {
		VotingService service = (VotingService) servletContext.getAttribute(WebConstants.ATTR_VOTING_SERVICE);
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
		int fromIndex = (page - 1) * size;

		if (fromIndex >= totalItems || fromIndex < 0 || size <= 0) {
			PagedResponse<Voting> emptyResponse = new PagedResponse<>(new ArrayList<>(), totalItems, page, size);
			return Response.ok(emptyResponse).build();
		}

		int toIndex = Math.min(fromIndex + size, totalItems);
		List<Voting> paginatedList = list.subList(fromIndex, toIndex);
		PagedResponse<Voting> pagedResponse = new PagedResponse<>(paginatedList, totalItems, page, size);
		return Response.ok(pagedResponse).build();
	}
}
