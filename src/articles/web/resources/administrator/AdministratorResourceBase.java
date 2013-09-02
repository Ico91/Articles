package articles.web.resources.administrator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import articles.dao.UserDAO;
import articles.model.User;
import articles.model.dto.UserDetails;
import articles.validators.MessageBuilder;
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
	protected HttpServletRequest request;

	public AdministratorResourceBase(@Context HttpServletRequest request) {
		this.request = request;
		this.logger = Logger.getLogger(getClass());
		this.userDAO = new UserDAO();
	}
}
