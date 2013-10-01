package articles.web.resources.users;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.dto.MessageDTO;
import articles.dto.UserDetails;
import articles.messages.RequestMessageKeys;
import articles.model.User;
import articles.web.listener.ConfigurationListener;
import articles.web.requests.users.UpdateUserRequest;

public class UsersSubResource extends UsersResourceBase {

	public UsersSubResource(HttpServletRequest request) {
		super(request);
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

		if (userToReturn == null) {
			logger.info("User with id = " + id + " not found");
			return Response.status(Status.NOT_FOUND).build();
		}

		logger.info("Administrator successfully view information "
				+ "for user with id = " + id);
		return Response.ok(userToReturn, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Update user information
	 * 
	 * @param id
	 *            ID of the user to update
	 * @param user
	 *            New user information
	 * @return Response with code 204 on success, 404 otherwise
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, final UserDetails user) {
		return new UpdateUserRequest(user, id).process();
	}

	/**
	 * Delete user with specified id
	 * 
	 * @param id
	 *            ID of the user to delete
	 * @return Response with code 204 on success, 400 if specified ID is ID of
	 *         the current administrator, 404 if user not found
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		if (isAdmin(id)) {
			MessageDTO dto = new MessageDTO();
			dto.addMessage(RequestMessageKeys.USER_CANNOT_DELETE_HIMSELF
					.getValue());
			logger.info("Admin whii id = " + id + " try to delete himself");

			return Response.status(Status.BAD_REQUEST).entity(dto).build();
		}

		if (!this.userDAO.deleteUser(id)) {
			logger.info("Failed to delete user with id = " + id);
			return Response.status(Status.NOT_FOUND).build();
		}

		logger.info("Deleted user with id = " + id);
		MessageDTO dto = new MessageDTO();
		dto.addMessage(RequestMessageKeys.USER_DELETED.getValue());
		return Response.ok(dto, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Check if passed ID is current admin ID
	 * 
	 * @param id
	 *            ID to check
	 * @return True if id is same as current admin
	 */
	private boolean isAdmin(int id) {
		int currentAdminId = Integer.parseInt(this.request.getSession()
				.getAttribute(ConfigurationListener.USERID).toString());
		return (id == currentAdminId) ? true : false;
	}
}
