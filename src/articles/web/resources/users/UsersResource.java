package articles.web.resources.users;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import articles.dao.StatisticsDAO;
import articles.dao.UserDAO;
import articles.dao.exceptions.StatisticsDAOException;
import articles.model.User;
import articles.model.dto.LoginRequest;
import articles.model.dto.UserDTO;
import articles.model.statistics.Event;
import articles.web.listener.SessionPathConfigurationListener;

/**Class for performing user requests
 * @author Galina Hristova
 *
 */
@Path("")

public class UsersResource {
	static final Logger logger = Logger.getLogger(UsersResource.class);
	@Context
	ServletContext context;
	
	/**
	 * If a user with the username and password, entered by a client, exists returns a response for success,
	 * otherwise returns response the the client is not authorized. On success a new session is created.
	 * @param loginRequest
	 * @param servletResponse
	 * @param servletRequest
	 * @return
	 * @throws ServletException
	 */
	@POST
	@Path("login")
	@Consumes({"application/xml, application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginRequest loginRequest,
			@Context HttpServletResponse servletResponse,
			@Context HttpServletRequest servletRequest) throws 
			ServletException {
		UserDAO userDAO = new UserDAO();
		User user = userDAO.getUser(loginRequest.getUsername(), loginRequest.getPassword());
		if (user != null) {
			userDAO.updateLastLogin(new Date(), user.getUserId());
			
			servletRequest.getSession().invalidate();
			HttpSession session = servletRequest.getSession();
			session.setAttribute("userId", user.getUserId());
			
			logger.info("User with id = " + user.getUserId() + " logged in the system.");
			
			try {
				StatisticsDAO statDao = new StatisticsDAO();
				statDao.save(user.getUserId(), Event.LOGIN);
			} catch (StatisticsDAOException e) {
				return Response.status(400).entity(e.getMessage()).build();
			}
			
			System.out.println(SessionPathConfigurationListener.getPath());
			
			return Response.ok(new UserDTO(user)).build();
		} else {
			logger.error("Unauthorized user tried to log in.");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	/**
	 * On success a user session is destroyed.
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
		if (session != null) {
			int userId = (int)session.getAttribute("userId");
			session.invalidate();
			logger.info("User with id = " + userId + " logged out from the system.");
			
			try {
				StatisticsDAO statDao = new StatisticsDAO();
				statDao.save(userId, Event.LOGOUT);
			} catch (StatisticsDAOException e) {
				return Response.status(400).entity(e.getMessage()).build();
			}
			
			return Response.ok().build();
		}
		logger.error("Unauthorized user tried to log out.");
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
}
