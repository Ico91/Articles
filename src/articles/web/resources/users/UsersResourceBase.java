package articles.web.resources.users;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import articles.dao.UserDAO;

/**
 * Base class for administrator resources
 * 
 * @author Krasimir Atanasov
 * 
 */
public class UsersResourceBase {
	protected UserDAO userDAO;
	protected final Logger logger;
	protected HttpServletRequest request;

	public UsersResourceBase(@Context HttpServletRequest request) {
		this.request = request;
		this.logger = Logger.getLogger(getClass());
		this.userDAO = new UserDAO();
	}
}
