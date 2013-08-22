package articles.web.resources.administrator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.dao.UserDAO;
import articles.model.User;

/**
 * 
 * @author Krasimir Atanasov
 * 
 */
public class AdministratorSubResource {
	
	/**
	 * Constructs new AdministratorSubResource object
	 */
	public AdministratorSubResource() {

	}

	/**
	 * Get user with specified ID.
	 * @param userId ID of the requested user
	 * @return Requested user
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("id") int userId) {
		UserDAO userDAO = new UserDAO();
		User user = userDAO.getUserById(userId);

		return (user != null) ? Response.ok(user, MediaType.APPLICATION_JSON)
				.build() : Response.status(Status.NOT_FOUND).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") int userId, User user) {
		UserDAO userDAO = new UserDAO();
		userDAO.u
	}
	
	
	
}
