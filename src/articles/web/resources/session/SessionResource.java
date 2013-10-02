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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import articles.dao.UserDAO;
import articles.dto.LoginRequest;
import articles.dto.MessageDTO;
import articles.dto.UserDTO;
import articles.messages.ErrorRequestMessageKeys;
import articles.model.User;
import articles.model.UserActivity;
import articles.web.listener.ConfigurationListener;
import articles.web.resources.DateAdapter;
import articles.web.resources.statistics.StatisticsResource;

/**
 * Class for performing user requests
 * 
 * @author Galina Hristova
 * 
 */
@Path("")
public class SessionResource {
	static final Logger logger = Logger.getLogger(SessionResource.class);
	private MessageDTO dto;
	@Context
	ServletContext context;

	//	TODO: Comments
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response userInfo(@Context HttpServletRequest servletRequest) {
		UserDAO dao = new UserDAO();
		int userId = (int) servletRequest.getSession(false).getAttribute(ConfigurationListener.USERID);
		User currentUser = dao.getUserById(userId);
		
		if(currentUser == null) {
			dto = new MessageDTO();
			dto.addMessage(ErrorRequestMessageKeys.FORBIDDEN.getValue());
			return Response.status(Status.FORBIDDEN).entity(dto).build();
		}

		return Response.ok(new UserDTO(currentUser), MediaType.APPLICATION_JSON).build();
	}
	
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

		System.out.println(uriInfo);
		UserDAO userDAO = new UserDAO();
		User user = userDAO.login(loginRequest.getUsername(),
				loginRequest.getPassword(), UserActivity.LOGIN, new Date());

		if (user == null) {
			dto = new MessageDTO();
			dto.addMessage(ErrorRequestMessageKeys.WRONG_LOGIN.getValue());
			logger.error("Unauthorized user tried to log in.");
			return Response.status(Response.Status.UNAUTHORIZED).entity(dto).build();
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
			dto = new MessageDTO();
			dto.addMessage(ErrorRequestMessageKeys.LOGOUT_ERROR.getValue());
			logger.error("Unauthorized user tried to log out.");
			return Response.status(Response.Status.UNAUTHORIZED).entity(dto).build();
		}

		int userId = (int) session.getAttribute(ConfigurationListener.USERID);
		session.invalidate();
		userDAO.logout(userId, UserActivity.LOGOUT);

		logger.info("User with id = " + userId + " logged out from the system.");

		return Response.noContent().build();

	}

	// TODO: Comments
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
			@QueryParam("activity") UserActivity activity,
			@QueryParam("from") int from, 
			@QueryParam("to") int to,
			@Context HttpServletRequest servletRequest) {

		int userId = (int) servletRequest.getSession(false).getAttribute(ConfigurationListener.USERID);
		return new StatisticsResource().getUserStatistics(userId, dateInput, activity, from, to);
	}
}
