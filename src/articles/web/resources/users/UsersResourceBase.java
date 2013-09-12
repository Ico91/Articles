package articles.web.resources.users;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import articles.dao.UserDAO;
import articles.model.User;

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
	protected List<User> users;

	public UsersResourceBase(@Context HttpServletRequest request) {
		this.request = request;
		this.logger = Logger.getLogger(getClass());
		this.userDAO = new UserDAO();
		this.users = userDAO.getUsers();
	}
}
