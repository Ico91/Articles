package articles.web.resources.administrator;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import articles.dao.ArticlesDAO;
import articles.dao.UserDAO;
import articles.model.Articles.Article;
import articles.model.User;
import articles.model.dto.NewUserRequest;
import articles.web.listener.ConfigurationListener;

/**
 * Class used to process all administrator requests
 * 
 * @author Krasimir Atanasov
 * 
 */
@Path("")
public class AdministratorResource {

	private UserDAO userDAO;
	private final Logger logger;

	public AdministratorResource() {
		this.userDAO = new UserDAO();
		logger = Logger.getLogger(getClass());
	}

	/**
	 * Get information of all existing users
	 * 
	 * @return List of all users
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		logger.info("Administrator get list of all users");
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

		// TODO: Change to correct ID
		createUserArticlesFile(0);

		return (this.userDAO.addUser(userToAdd)) ? Response.ok().build()
				: Response.status(Status.BAD_REQUEST).build();
	}

	@Path("{id}")
	public AdministratorSubResource getAdministratorSubResource() {
		return new AdministratorSubResource(this.userDAO);
	}

	/**
	 * Create empty articles file for user with specified ID
	 * 
	 * @param userId
	 *            User ID
	 */
	private void createUserArticlesFile(int userId) {
		String path = ConfigurationListener.getPath();

		// TODO: Duplicated [ArticlesResourceBase - getArticlesPath]
		// FIXME: Move to Configuration Listener
		if (path == null) {
			String message = "Cannot read articles file path.";
			throw new RuntimeException(message);
		}

		ArticlesDAO articlesDAO = new ArticlesDAO(path);
		articlesDAO.saveArticles(userId, new ArrayList<Article>());

		logger.info("Administrator created new articles file for user with id = "
				+ userId + " at " + path);
	}
}
