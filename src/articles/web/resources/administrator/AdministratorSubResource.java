package articles.web.resources.administrator;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.dao.ArticlesDAO;
import articles.dao.UserDAO;
import articles.model.User;
import articles.model.dto.NewUserRequest;
import articles.web.listener.ConfigurationListener;

public class AdministratorSubResource extends AdministratorResourceBase {

	public AdministratorSubResource() {
		super();
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
	 * @return Response with code 200 on success, 400 otherwise
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, final NewUserRequest user) {
		List<User> users = this.userDAO.getUsers();
		final int userId = id;
		
		//	Remove user from list of all users
		for(int i = 0; i < users.size(); i ++) {
			if(users.get(i).getUserId() == id) {
				users.remove(i);
			}
		}
		
		return validateAndExecute(user, users, new Executable() {
			
			@Override
			public Response execute(UserDAO userDAO) {
				if (!userDAO.updateUser(userId, user)) {
					logger.info("Failed to update user with id = " + userId);
					return Response.status(Status.NOT_FOUND).build();
				}

				logger.info("Updated user with id = " + userId);
				return Response.ok().build();
			}
		});
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

		if (!this.userDAO.deleteUser(id)) {
			logger.info("Failed to delete user with id = " + id);
			return Response.status(Status.NOT_FOUND).build();
		}

		//	Remove articles file of deleted user
		ArticlesDAO articlesDAO = new ArticlesDAO(ConfigurationListener.getPath());
		articlesDAO.deleteUserArticlesFile(id);
		
		logger.info("Deleted user with id = " + id);
		return Response.ok().build();
	}
}
