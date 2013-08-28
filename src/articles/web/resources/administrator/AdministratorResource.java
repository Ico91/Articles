package articles.web.resources.administrator;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.dao.UserDAO;
import articles.model.User;
import articles.model.dto.NewUserRequest;

/**
 * Class used to process all administrator requests
 * 
 * @author Krasimir Atanasov
 * 
 */
@Path("")
public class AdministratorResource {

	private UserDAO userDAO;
	
	public AdministratorResource() {
		this.userDAO = new UserDAO();
	}

	/**
	 * Get information of all existing users
	 * 
	 * @return List of all users
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		return this.userDAO.getUsers();
	}

	/**
	 * Create new user
	 * 
	 * @param userToAdd
	 *            User to create
	 * @return Response with status code 200 on success, 400 on error
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(NewUserRequest userToAdd) {
		// TODO: Validations, unique username
		
		// TODO: Create articles file
		
		
		return (this.userDAO.addUser(userToAdd)) ? Response.ok().build()
				: Response.status(Status.BAD_REQUEST).build();
	}

	@Path("{id}")
	public AdministratorSubResource getAdministratorSubResource() {
		return new AdministratorSubResource();
	}
}
