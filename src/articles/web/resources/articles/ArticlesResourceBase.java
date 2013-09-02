package articles.web.resources.articles;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import articles.dao.ArticlesDAO;
import articles.model.Articles.Article;
import articles.validators.ArticleValidator;
import articles.validators.MessageBuilder;
import articles.validators.MessageKey;
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
	// TODO potential thread issues here
	protected List<Article> articles;
	protected ArticlesDAO dao;
	protected int userId;

	public ArticlesResourceBase(@Context HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
		this.dao = new ArticlesDAO(ConfigurationListener.getPath());
		this.userId = getUserId();
		this.articles = this.dao.loadArticles(this.userId);
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
