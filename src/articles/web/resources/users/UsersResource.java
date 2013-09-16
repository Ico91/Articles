package articles.web.resources.users;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.builders.ResultBuilder;
import articles.dao.ArticlesDAO;
import articles.dto.UserDetails;
import articles.model.User;
import articles.validators.PaginationParamValidator;
import articles.validators.UserValidator;
import articles.web.listener.ConfigurationListener;
import articles.web.resources.PageRequest;
import articles.web.resources.ResourceRequest;

/**
 * Class used to process all administrator requests
 * 
 * @author Krasimir Atanasov
 * 
 */
@Path("")
public class UsersResource extends UsersResourceBase {
	public UsersResource(@Context HttpServletRequest request) {
		super(request);
	}

	/**
	 * Get information of all existing users, or if search term is provided
	 * returned list is based on found results.
	 * 
	 * @return List of all users
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@QueryParam("search") String searchTerm,
			@QueryParam("from") final int from, @QueryParam("to") final int to) {
		// TODO: Use JPA methods

		List<User> listOfUsers = (searchTerm == null) ? this.users : search(
				searchTerm, this.users);

		return new PageRequest<User>() {

			@Override
			public Response doProcess(List<User> listOfObjects) {
				return Response.ok(
						new ResultBuilder<User>().buildResult(listOfObjects,
								from, to), MediaType.APPLICATION_JSON).build();
			}
		}.process(listOfUsers, new PaginationParamValidator(from, to,
				listOfUsers.size()));
	}

	/**
	 * Returns a list of users based on the results of the search by username.
	 * 
	 * @param searchTerm
	 * @param users
	 *            - the container to search into
	 * @return List of found {@link articles.model.User}
	 */
	private List<User> search(String searchTerm, List<User> users) {
		List<User> usersToReturn = new ArrayList<User>();

		for (User u : users) {
			if (u.getUsername().contains(searchTerm)) {
				usersToReturn.add(u);
			}
		}

		return usersToReturn;
	}

	/**
	 * Create new user
	 * 
	 * @param userToAdd
	 *            User to create
	 * @return Response with status code 204 on success, 400 on error
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(final UserDetails userToAdd) {
		return new ResourceRequest<UserDetails, User>() {

			@Override
			public Response doProcess(UserDetails objectToValidate,
					List<User> listOfObjects) {
				User user = userDAO.addUser(userToAdd);
				if (user == null) {
					logger.info("Failed to create user");
					return Response.status(Status.BAD_REQUEST).build();
				}

				// Create new articles file for user
				ArticlesDAO articlesDAO = new ArticlesDAO(
						ConfigurationListener.getPath());
				articlesDAO.createUserArticlesFile(user.getUserId());

				logger.info("Created user " + user.getUsername()
						+ " with id = " + user.getUserId());
				return Response.ok(user).build();
			}
		}.process(userToAdd, this.users, new UserValidator(userToAdd,
				this.users));
	}

	@Path("{id}")
	public UsersSubResource getAdministratorSubResource() {
		return new UsersSubResource(request);
	}
}
