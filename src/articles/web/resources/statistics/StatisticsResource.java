package articles.web.resources.statistics;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import articles.dao.StatisticsDAO;
import articles.dto.ResultDTO;
import articles.dto.UserStatisticsDTO;
import articles.model.UserActivity;
import articles.model.UserStatistics;
import articles.utils.ModelToDTOTransformer;
import articles.web.resources.DateAdapter;
import articles.web.resources.PageRequest;

@Path("")
public class StatisticsResource {
	// TODO: Comments
	/**
	 * Returns statistics information for all users for specific date.
	 * 
	 * @param dateInput
	 *            - date to load the statistics for. The specified date is
	 *            required in the ISO format (yyyy/mm/dd)
	 * @return Map containing all user ids as keys and List of
	 *         {@link articles.model.dto.UserStatisticsDTO } as values
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatistics(
			@QueryParam("date") final DateAdapter dateInput,
			@QueryParam("activity") final UserActivity activity,
			@QueryParam("from") final int from, @QueryParam("to") final int to) {

		return statisticsPageRequest(null, dateInput, activity, from, to);
	}

	// TODO: Comments
	/**
	 * Returns statistics information according to the specified user.
	 * 
	 * @param userIdRequest
	 *            - id of the requested user's statistics
	 * @param dateInput
	 *            - date to load the statistics for. The specified date is
	 *            required in the ISO format (yyyy/mm/dd)
	 * @return List of {@link articles.model.dto.UserStatisticsDTO }
	 */
	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserStatistics(@PathParam("userId") final int userId,
			@QueryParam("date") final DateAdapter dateInput,
			@QueryParam("activity") final UserActivity activity,
			@QueryParam("from") final int from, @QueryParam("to") final int to) {

		return statisticsPageRequest(userId, dateInput, activity, from, to);
	}

	// TODO: Comments
	private Response statisticsPageRequest(final Integer userId,
			final DateAdapter dateInput, final UserActivity activity,
			final int from, final int to) {
		return new PageRequest<UserStatistics>() {

			@Override
			public Response doProcess(int from, int to) {
				StatisticsDAO dao = new StatisticsDAO();
				Date date = (dateInput != null) ? dateInput.getDate() : null;

				int totalResults = (userId != null) ?
						dao.loadUserStatistics(userId, date, activity, 0, 0).size() : 
						dao.loadStatistics(date, activity, 0, 0).size();
						
				List<UserStatistics> listOfUserStatistics = (userId != null) ?
						dao.loadUserStatistics(userId, date, activity, from, to) :
						dao.loadStatistics(date, activity, from, to);

				return Response
						.ok(new ResultDTO<UserStatisticsDTO>(
								ModelToDTOTransformer.fillListOfStatisticsDTO(listOfUserStatistics),
								totalResults), MediaType.APPLICATION_JSON)
						.build();

			}
		}.process(from, to);
	}

}
