package articles.web.resources.articles;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import articles.dao.ArticlesDAO;
import articles.dao.exceptions.ArticlesDAOException;
import articles.model.Articles.Article;
import articles.model.dto.ErrorMessage;
import articles.model.dto.validators.ArticleValidator;
import articles.model.dto.validators.MessageBuilder;
import articles.model.dto.validators.MessageKeys;
import articles.web.resources.exception.ArticlesResourceException;
import articles.web.resources.users.UsersResource;

/**
 * Class used to process request to specified article
 * 
 * @author Krasimir Atanasov
 * 
 */
public class ArticleSubResource {
	private int userId;
	private ArticlesDAO dao;
	private ArticleValidator articleValidator;
	static final Logger logger = Logger.getLogger(UsersResource.class);

	/**
	 * Constructs new ArticlesSubResource object
	 * 
	 * @param dao
	 *            Articles data access object
	 * @param userId
	 *            ID of logged user
	 */
	public ArticleSubResource(ArticlesDAO dao, int userId) {
		this.userId = userId;
		this.dao = dao;
		this.articleValidator = new ArticleValidator();
	}

	/**
	 * Returns article with specified ID If article exists: returns response
	 * code 200, else returns response code 404
	 * 
	 * @param id
	 *            Requested article ID
	 * @return Article with requested ID
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticle(@PathParam("id") int id) {
		try {
			Article article = this.dao.getArticleById(userId, id);
			if (article != null) {
				logger.info("User with id = " + userId
						+ " opened an article with id = " + id + ".");
				return Response.ok(article, MediaType.APPLICATION_JSON).build();
			} else {
				logger.info("User with id = " + userId
						+ " request article that does not exist.");
				return Response.status(Status.NOT_FOUND).build();
			}

		} catch (ArticlesDAOException e) {
			logger.error("User with id = " + userId
					+ " failed to open an article with id = " + id + ".");
			throw e;
		}
	}

	/**
	 * Update article with specified ID If article with specified ID is found -
	 * response code 200, else response code 304. If article format is invalid -
	 * response code 418
	 * 
	 * @param article
	 *            Article with updated title and/or content
	 * @param id
	 *            Article ID
	 * @return Response containing the response code
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateArticle(Article article, @PathParam("id") int id) {
		article.setId(id);

		try {
			validateArticle(article);
			boolean result = this.dao.updateArticle(this.userId, article);
			if (result)
				logger.info("User with id = " + userId
						+ " updated an article with id = " + id + ".");

			return (result) ? Response.ok().build() : Response.status(
					Status.NOT_MODIFIED).build(); //TODO NOT_MODIFIED ?

		} catch (ArticlesDAOException e) {
			logger.error("User with id = " + userId
					+ " failed to update an article with id = " + id + ".");
			throw e;
		} catch (ArticlesResourceException e) {
			return Response.status(Status.BAD_REQUEST)
					.entity(new ErrorMessage(e.getMessage())).build();
		}
	}

	/**
	 * Delete article with specified ID Returns response code 200 when article
	 * was successfully deleted, response code 404 when article is not found and
	 * response code 418 if any other error occur
	 * 
	 * @param id
	 *            Article ID
	 * @return Response with response code
	 */
	@DELETE
	public Response deleteArticle(@PathParam("id") int id) {
		try {
			boolean result = this.dao.deleteArticle(userId, id);
			if (result) {
				logger.info("User with id = " + userId
						+ " deleted an article with id = " + id + ".");

				return Response.ok().build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}

		} catch (ArticlesDAOException e) {
			logger.error("User with id = " + userId
					+ " failed to delete an article with id = " + id + ".");
			throw e;
		}
	}

	/**
	 * Validate article format
	 * 
	 * @param article
	 *            Article to validate
	 */
	// TODO: :(
	private void validateArticle(Article article) {
		List<MessageKeys> messageKeys = this.articleValidator.validate(article);

		if (messageKeys.size() != 0) {
			MessageBuilder builder = new MessageBuilder(messageKeys);
			throw new ArticlesResourceException(builder.getMessage());
		}
	}

}
