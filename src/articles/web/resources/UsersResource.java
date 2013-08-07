package articles.web.resources;

import java.io.IOException;

import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import articles.dao.UserDAO;
import articles.model.User;

@Path("")
public class UsersResource {
	@Context
	ServletContext context;
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(String username, String password,
			@Context HttpServletResponse servletResponse,
			@Context HttpServletRequest servletRequest) throws IOException, ServletException {
		
    	
		UserDAO userDAO = new UserDAO();
		try {
			User user = userDAO.find(username, password);
			
			HttpSession session = servletRequest.getSession();
			session.setAttribute("userId", user.getUserId());
			return Response.ok(user.toString(), MediaType.APPLICATION_JSON).build();
		} catch (NoResultException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("User not found ").build();
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
