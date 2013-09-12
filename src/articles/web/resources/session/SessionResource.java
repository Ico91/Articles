package articles.web.resources.session;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import articles.dao.StatisticsDAO;
import articles.dao.UserDAO;
import articles.dto.LoginRequest;
import articles.dto.UserDTO;
import articles.model.User;
import articles.model.UserActivity;
import articles.web.listener.ConfigurationListener;
import articles.web.resources.DateAdapter;
import articles.web.resources.StatisticsRequest;

import com.google.gson.Gson;

/**
 * Class for performing user requests
 * 
 * @author Galina Hristova
 * 
 */
@Path("")
public class SessionResource {
	static final Logger logger = Logger.getLogger(SessionResource.class);
	@Context
	ServletContext context;

	/**
	 * If a user with the username and password, entered by a client, exists
	 * returns a response for success, otherwise returns response the the client
	 * is not authorized. On success a new session is created.
	 * 
	 * @param loginRequest
	 * @param servletResponse
	 * @param servletRequest
	 * @return
	 * @throws ServletException
	 */
	@POST
	@Path("login")
	@Consumes({ "application/xml, application/json" })
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginRequest loginRequest,
			@Context HttpServletResponse servletResponse,
			@Context HttpServletRequest servletRequest, @Context UriInfo uriInfo)
			throws ServletException {

		UserDAO userDAO = new UserDAO();
		User user = userDAO.login(loginRequest.getUsername(),
				loginRequest.getPassword(), UserActivity.LOGIN, new Date());

		if (user == null) {
			logger.error("Unauthorized user tried to log in.");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		servletRequest.getSession().invalidate();
		HttpSession session = servletRequest.getSession();
		session.setAttribute(ConfigurationListener.USERID, user.getUserId());
		session.setAttribute(ConfigurationListener.USERTYPE, user.getUserType());

		logger.info("User with id = " + user.getUserId()
				+ " logged in the system.");

		return Response.ok(new UserDTO(user)).build();

	}

	/**
	 * On success a user session is destroyed.
	 * 
	 * @param servletResponse
	 * @param servletRequest
	 * @return
	 */
	@POST
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@Context HttpServletResponse servletResponse,
			@Context HttpServletRequest servletRequest) {

		HttpSession session = servletRequest.getSession(false);
		UserDAO userDAO = new UserDAO();

		if (session == null) {
			logger.error("Unauthorized user tried to log out.");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		int userId = (int) session.getAttribute(ConfigurationListener.USERID);
		session.invalidate();
		userDAO.logout(userId, UserActivity.LOGOUT);

		logger.info("User with id = " + userId + " logged out from the system.");

		return Response.noContent().build();

	}

	/**
	 * Returns statistics information for the currently logged user.
	 * 
	 * @param dateInput
	 *            - date to load the statistics for. The specified date is
	 *            required in the ISO format (yyyy/mm/dd)
	 * @return If successful returns List of
	 *         {@link articles.model.dto.UserStatisticsDTO }, otherwise returns
	 *         status code 400 (Bad request).
	 */
	@GET
	@Path("statistics")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatistics(@QueryParam("date") DateAdapter dateInput,
			@Context final HttpServletRequest servletRequest) {

		return new StatisticsRequest() {

			@Override
			public Response execute(Date dateInput) {
				Gson gson = new Gson();
				StatisticsDAO statisticsDAO = new StatisticsDAO();
				return Response
						.ok()
						.entity(gson.toJson(statisticsDAO.load(
								(int) servletRequest.getSession(false)
										.getAttribute(
												ConfigurationListener.USERID),
								dateInput))).build();
			}

		}.getStatistics(dateInput);
	}
}