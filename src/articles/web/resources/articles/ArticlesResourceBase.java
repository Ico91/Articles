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
import articles.validators.ErrorMessageBuilder;
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
	 * Validate article format and return appropriate response If article format
	 * is valid return null
	 * 
	 * @param article
	 * @return Response 404 with information about validation errors
	 */
	public Response validationResponse(Article article) {
		ArticleValidator validator = new ArticleValidator(article, articles);
		List<MessageKey> messageKeys = validator.validate();

		if (!messageKeys.isEmpty()) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity(new ErrorMessageBuilder(messageKeys)
							.getErrorMessage()).build();
		}

		return null;
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
