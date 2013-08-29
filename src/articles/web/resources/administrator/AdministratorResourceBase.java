package articles.web.resources.administrator;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import articles.dao.UserDAO;
import articles.model.User;
import articles.model.dto.NewUserRequest;
import articles.validators.ErrorMessageBuilder;
import articles.validators.MessageKey;
import articles.validators.UserValidator;

/**
 * Base class for administrator resources
 * 
 * @author Krasimir Atanasov
 * 
 */
public class AdministratorResourceBase {
	protected UserDAO userDAO;
	protected final Logger logger;

	public AdministratorResourceBase() {
		this.logger = Logger.getLogger(getClass());
		this.userDAO = new UserDAO();
	}

	/**
	 * Validate user and return appropriate response
	 * 
	 * @param userToCheck
	 * @param users
	 * @return null if the user is correct otherwise Response
	 */
	protected Response validationResponse(NewUserRequest userToCheck,
			List<User> users) {

		UserValidator validator = new UserValidator(userToCheck, users);
		List<MessageKey> messageKeys = validator.validate();

		if (!messageKeys.isEmpty()) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity(new ErrorMessageBuilder(messageKeys)
							.getErrorMessage()).build();
		}

		return null;
	}

	protected interface Executable {
		Response execute(UserDAO userDAO);
	}

	protected Response validateAndExecute(final NewUserRequest request,
			List<User> users, Executable executable) {

		Response validationResponse = validationResponse(request, users);

		if (validationResponse != null) {
			logger.info("Invalid request format");
			return validationResponse;
		}

		return executable.execute(this.userDAO);
	}
}
