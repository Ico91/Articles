package articles.web.resources.users;

import java.io.IOException;

import javax.persistence.NoResultException;
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

import articles.dao.UserDAO;
import articles.model.User;
import articles.model.dto.LoginRequest;
import articles.model.dto.UserDTO;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.LoggingFilter;

@Path("")

public class UsersResource {
	@Context
	ServletContext context;

	@POST
	@Path("login")
	@Consumes({"application/xml","application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public UserDTO login(LoginRequest loginRequest,
			@Context HttpServletResponse servletResponse,
			@Context HttpServletRequest servletRequest) throws IOException,
			ServletException {
		UserDAO userDAO = new UserDAO();
		Client client = Client.create();
		client.addFilter(new LoggingFilter(System.out));
		try {
			User user = userDAO.find(loginRequest.getUsername(), loginRequest.getPassword());
			HttpSession session = servletRequest.getSession();
			session.setAttribute("userId", user.getUserId());
			return new UserDTO(user);
		} catch (NoResultException e) {
			return new UserDTO();
		}
	}

	@POST
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@Context HttpServletResponse servletResponse,
			@Context HttpServletRequest servletRequest) {
		HttpSession session = servletRequest.getSession(false);
		if (session != null) {
			session.invalidate();
			return Response.ok().build();

		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
}
