package articles.web.resources.users;

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

import articles.dto.UserDetails;
import articles.web.requests.users.AddUserRequest;
import articles.web.requests.users.UsersPageRequest;

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
	public Response getUsers(@QueryParam("search") final String searchTerm,
			@QueryParam("from") final int from, @QueryParam("to") final int to) {

		return new UsersPageRequest(from, to, userDAO, searchTerm).process();
	}

	/**
	 * Returns a list of users based on the results of the search by username.
	 * 
	 * @param searchTerm
	 * @param users
	 *            - the container to search into
	 * @return List of found {@link articles.model.User}
	 */
	// private List<User> search(String searchTerm, List<User> users) {
	// List<User> usersToReturn = new ArrayList<User>();
	//
	// for (User u : users) {
	// if (u.getUsername().contains(searchTerm)) {
	// usersToReturn.add(u);
	// }
	// }
	//
	// return usersToReturn;
	// }

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
		return new AddUserRequest(userToAdd).process();
	}

	@Path("{id}")
	public UsersSubResource getAdministratorSubResource() {
		return new UsersSubResource(request);
	}
}
