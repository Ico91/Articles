package articles.web.resources.statistics;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import articles.dao.StatisticsDAO;
import articles.model.UserActivity;
import articles.web.resources.DateAdapter;
import articles.web.resources.StatisticsRequest;

import com.google.gson.Gson;

@Path("")
public class StatisticsResource {
	private Gson gson = new Gson();
	private StatisticsDAO statisticsDAO = new StatisticsDAO();

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
			@QueryParam("activity") UserActivity activity) {
		return new StatisticsRequest() {

			@Override
			public Response execute(Date dateInput, UserActivity activity) {

				return Response
						.ok()
						.entity(gson.toJson(statisticsDAO.loadAll(dateInput,
								activity))).build();
			}
		}.getStatistics(dateInput, activity);
	}

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
			@QueryParam("activity") UserActivity activity) {
		return new StatisticsRequest() {

			@Override
			public Response execute(Date dateInput, UserActivity activity) {
				return Response
						.ok()
						.entity(gson.toJson(statisticsDAO.load(userId,
								dateInput, activity))).build();
			}

		}.getStatistics(dateInput, activity);
	}

}
