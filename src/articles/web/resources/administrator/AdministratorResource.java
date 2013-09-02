package articles.web.resources.administrator;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.dao.ArticlesDAO;
import articles.dao.StatisticsDAO;
import articles.dao.UserDAO;
import articles.model.User;
import articles.model.dto.UserDetails;
import articles.web.listener.ConfigurationListener;
import articles.web.resources.statistics.DateAdapter;
import articles.web.resources.statistics.StatisticsRequest;

import com.google.gson.Gson;

/**
 * Class used to process all administrator requests
 * 
 * @author Krasimir Atanasov
 * 
 */
@Path("")
public class AdministratorResource extends AdministratorResourceBase {
	private Gson gson = new Gson();
	private StatisticsDAO statisticsDAO = new StatisticsDAO();

	public AdministratorResource(@Context HttpServletRequest request) {
		super(request);
	}

	/**
	 * Get information of all existing users
	 * 
	 * @return List of all users
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		logger.info("Administrator readed all users info");
		return this.userDAO.getUsers();
	}

	/**
	 * Returns statistics information for all users.
	 * 
	 * @param dateInput
	 *            - date to load the statistics for. The specified date is
	 *            required in the ISO format (yyyy/mm/dd)
	 * @return Map containing all user ids as keys and List of
	 *         {@link articles.model.dto.UserStatisticsDTO } as values
	 */
	@GET
	@Path("/statistics")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatistics(@QueryParam("date") DateAdapter dateInput) {
		return new StatisticsRequest() {

			@Override
			public Response execute(Date dateInput) {
				return Response.ok()
						.entity(gson.toJson(statisticsDAO.load(dateInput)))
						.build();
			}
		}.getStatistics(dateInput);
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
	@Path("/statistics/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserStatistics(@PathParam("userId") final int userId,
			@QueryParam("date") DateAdapter dateInput) {
		return new StatisticsRequest() {

			@Override
			public Response execute(Date dateInput) {
				return Response
						.ok()
						.entity(gson.toJson(statisticsDAO.load(userId,
								dateInput))).build();
			}

		}.getStatistics(dateInput);
	}

	/**
	 * Create new user
	 * 
	 * @param userToAdd
	 *            User to create
	 * @return Response with status code 204 on success, 400 on error
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(final UserDetails userToAdd) {
		return validateAndExecute(userToAdd, this.userDAO.getUsers(),
				new Executable() {
					@Override
					public Response execute(UserDAO userDAO) {
						User user = userDAO.addUser(userToAdd);
						if (user == null) {
							logger.info("Failed to create user");
							return Response.status(Status.BAD_REQUEST).build();
						}

						// Create new articles file for user
						ArticlesDAO articlesDAO = new ArticlesDAO(
								ConfigurationListener.getPath());
						articlesDAO.createUserArticlesFile(user.getUserId());

						logger.info("Created user " + user.getUsername()
								+ " with id = " + user.getUserId());
						return Response.noContent().build();
					}
				});
	}

	@Path("{id}")
	public AdministratorSubResource getAdministratorSubResource() {
		return new AdministratorSubResource(request);
	}
}
