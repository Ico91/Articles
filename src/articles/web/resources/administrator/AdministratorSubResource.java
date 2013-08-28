package articles.web.resources.administrator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.dao.UserDAO;
import articles.model.User;

public class AdministratorSubResource {

	private UserDAO userDAO;

	public AdministratorSubResource(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * Get user information based on specified id
	 * 
	 * @param id
	 *            ID of the requested user information
	 * @return User information
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@PathParam("id") int id) {
		User userToReturn = this.userDAO.getUserById(id);

		return (userToReturn != null) ? Response.ok(userToReturn,
				MediaType.APPLICATION_JSON).build() : Response.status(
				Status.NOT_FOUND).build();
	}

	/**
	 * Update user information
	 * 
	 * @param id
	 *            ID of the user to update
	 * @param user
	 *            New user information
	 * @return Response with code 200 on success, 400 otherwise
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, User user) {
		// TODO: Validations

		return (this.userDAO.updateUser(id, user)) ? Response.ok().build()
				: Response.status(Status.NOT_FOUND).build();
	}

	/**
	 * Delete user with specified id
	 * 
	 * @param id
	 *            ID of the user to delete
	 * @return Response with code 200 on success, 400 otherwise
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		return (this.userDAO.deleteUser(id)) ? Response.ok().build() : Response
				.status(Status.NOT_FOUND).build();
	}
}
