package articles.web.resources.articles;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import articles.dao.ArticlesDAO;
import articles.dao.UserDAO;
import articles.web.listener.ConfigurationListener;

/**
 * Base class for article resources
 * 
 * @author Krasimir Atanasov
 * 
 */
public abstract class ArticlesResourceBase {
	protected final Logger logger = Logger.getLogger(getClass());

	protected HttpServletRequest servletRequest;
	protected List<Integer> userIds;
	protected ArticlesDAO articlesDao;
	protected int userId;
	
	public ArticlesResourceBase(@Context HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
		this.articlesDao = new ArticlesDAO(ConfigurationListener.getPath());
		this.userIds = new UserDAO().getListOfUserIds();
		this.userId = getUserId();
	}

	/**
	 * Get user ID from session
	 * 
	 * @return User ID
	 */
	private int getUserId() {
		return (int) servletRequest.getSession().getAttribute(
				ConfigurationListener.USERID);
	}
}
