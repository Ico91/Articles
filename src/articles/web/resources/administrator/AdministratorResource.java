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
import articles.validators.ErrorMessageBuilder;
import articles.validators.MessageKey;
import articles.validators.UserValidator;
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
	private List<User> users;

	public AdministratorResource() {
		this.userDAO = new UserDAO();
		logger = Logger.getLogger(getClass());
		users = userDAO.getUsers();
	}

	/**
	 * Get information of all existing users
	 * 
	 * @return List of all users
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		logger.info("Administrator readed all users info");
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
		
		Response validationResponse = validationResponse(userToAdd);

		if (validationResponse != null) {
			logger.info("Invalid adding a new user");
			return validationResponse;
		}
		
		User user = this.userDAO.addUser(userToAdd);
		if (user == null) {
			logger.info("Failed to create user");
			return Response.status(Status.BAD_REQUEST).build();
		}

		createUserArticlesFile(user.getUserId());
		logger.info("Created user " + user.getUsername() + " with id = "
				+ user.getUserId());
		return Response.ok().build();

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
		ArticlesDAO articlesDAO = new ArticlesDAO(
				ConfigurationListener.getPath());
		articlesDAO.saveArticles(userId, new ArrayList<Article>());
	}
	
	private Response validationResponse(NewUserRequest userToCheck) {
		UserValidator validator = new UserValidator(userToCheck, users);
		List<MessageKey> messageKeys = validator.validate();

		if (!messageKeys.isEmpty()) {
			return Response.status(Status.BAD_REQUEST)
					.entity(new ErrorMessageBuilder(messageKeys).getMessage())
					.build();
		}

		return null;
	}
}
