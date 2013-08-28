package articles.web.resources.statistics;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import articles.dao.StatisticsDAO;
import articles.dao.UserDAO;
import articles.model.UserType;
import articles.web.listener.ConfigurationListener;

/**
 * Provides methods for accessing statistics of user activities individually or
 * global statistics of all users.
 * 
 * @author Hristo
 * 
 */
@Path("")
public class StatisticsResource {
	private int userId;
	private Gson gson;
	private UserDAO userDAO;
	private StatisticsDAO statisticsDAO;

	public StatisticsResource(@Context HttpServletRequest servletRequest) {
		this.statisticsDAO = new StatisticsDAO();
		this.userDAO = new UserDAO();
		this.userId = (int) servletRequest.getSession(false).getAttribute(
				ConfigurationListener.USERID);
		this.gson = new Gson();
	}

	/**
	 * Returns statistics information for the currently logged user. If he's of
	 * type administrator returned statistics are for all users.
	 * 
	 * @param dateInput
	 *            - date to load the statistics for. The specified date is
	 *            required in the ISO format (yyyy/mm/dd)
	 * @return If successful returns List of
	 *         {@link articles.model.dto.UserStatisticsDTO }, otherwise returns
	 *         status code 400 (Bad request).
	 */
	@GET
	@Produces("application/json")
	public Response getStatistics(
			@QueryParam("date") final DateAdapter dateInput) {

		return executeRequest(dateInput, new StatisticsTask() {

			@Override
			public Response execute() {
				if (getUserType().equals(UserType.USER)) {
					return Response
							.ok()
							.entity(gson.toJson(statisticsDAO.load(userId,
									dateInput.getDate()))).build();
				}

				return Response
						.ok()
						.entity(gson.toJson(statisticsDAO.load(dateInput
								.getDate()))).build();
			}

		});
	}

	/**
	 * Returns statistics information according to the specified user. All user
	 * statistics are available for the administrator and only individual
	 * statistics for the user himself.
	 * 
	 * @param userIdRequest
	 *            - id of the requested user's statistics
	 * @param dateInput
	 *            - date to load the statistics for. The specified date is
	 *            required in the ISO format (yyyy/mm/dd)
	 * @return List of {@link articles.model.dto.UserStatisticsDTO }
	 */
	@GET
	@Produces("application/json")
	@Path("/{userid}")
	public Response getUserStatistics(
			@PathParam("userid") final int userIdRequest,
			@QueryParam("date") final DateAdapter dateInput) {

		return executeRequest(dateInput, new StatisticsTask() {

			@Override
			public Response execute() {
				if (getUserType().equals(UserType.USER)
						&& userId != userIdRequest) {
					return Response
							.status(403)
							.entity("You don't have the neccessary rights to view this page!")
							.build();
				}
				return Response
						.ok()
						.entity(gson.toJson(statisticsDAO.load(userIdRequest,
								dateInput.getDate()))).build();
			}

		});
	}

	private UserType getUserType() {
		return userDAO.getUserById(userId).getUserType();
	}

	private Response executeRequest(DateAdapter dateInput, StatisticsTask task) {
		if (dateInput == null)
			return Response.status(400).entity("No date provided").build();

		return task.execute();

	}
}
