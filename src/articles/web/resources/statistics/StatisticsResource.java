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
import articles.dao.UserDAO;
import articles.dto.ResultDTO;
import articles.dto.UserStatisticsDTO;
import articles.model.UserActivity;
import articles.model.UserStatistics;
import articles.utils.ModelToDTOTransformer;
import articles.web.requests.PageRequest;
import articles.web.resources.DateAdapter;

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
	public Response getStatistics(@QueryParam("date") DateAdapter dateInput,
			@QueryParam("activity") UserActivity activity,
			@QueryParam("from") int from, @QueryParam("to") int to) {

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
			@QueryParam("date") DateAdapter dateInput,
			@QueryParam("activity") UserActivity activity,
			@QueryParam("from") int from, @QueryParam("to") final int to) {

		return statisticsPageRequest(userId, dateInput, activity, from, to);
	}

	// TODO: Comments + StatisticsPageRequest
	private Response statisticsPageRequest(final Integer userId,
			final DateAdapter dateInput, final UserActivity activity,
			final int from, final int to) {

		return new PageRequest(from, to) {

			@Override
			protected Object doProcess() {
				StatisticsDAO statisticsDao = new StatisticsDAO();
				UserDAO userDao = new UserDAO();

				Date date = (dateInput != null) ? dateInput.getDate() : null;

				int totalResults = (userId != null) ? statisticsDao
						.loadUserStatistics(userId, date, activity, 0, 0)
						.size() : statisticsDao.loadStatistics(date, activity,
						0, 0).size();

				List<UserStatistics> listOfUserStatistics = (userId != null) ? statisticsDao
						.loadUserStatistics(userId, date, activity, from, to)
						: statisticsDao
								.loadStatistics(date, activity, from, to);

				return new ResultDTO<UserStatisticsDTO>(
						ModelToDTOTransformer.fillListOfStatisticsDTO(
								listOfUserStatistics, userDao.getUsersMap()),
						totalResults);

			}
		}.process();
	}

}
