package articles.web.resources.articles;

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
import articles.dao.StatisticsDAO;
import articles.dao.exceptions.ArticlesDAOException;
import articles.dao.exceptions.StatisticsDAOException;
import articles.model.Articles.Article;
import articles.model.statistics.UserActivity;
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
			Article article = this.dao.getArticle(userId, id);
			logger.info("User with id = " + userId
					+ " opened an article with id = " + id + ".");
			return Response.ok(article, MediaType.APPLICATION_JSON).build();

		} catch (ArticlesDAOException e) {
			logger.error("User with id = " + userId
					+ " failed to open an article with id = " + id + ".");
			return Response.status(Status.NOT_FOUND).build();
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
			boolean result = this.dao.updateArticle(this.userId, article);
			if (result)
				logger.info("User with id = " + userId
						+ " updated an article with id = " + id + ".");
			
			try {
				StatisticsDAO statDao = new StatisticsDAO();
				statDao.save(this.userId, UserActivity.MODIFY_ARTICLE);
			} catch (StatisticsDAOException e) {
				return Response.status(400).entity(e.getMessage()).build();
			}

			return (result == true) ? Response.ok().build() : Response.status(
					Status.NOT_MODIFIED).build();

		} catch (ArticlesDAOException e) {
			logger.error("User with id = " + userId
					+ " failed to update an article with id = " + id + ".");
			return Response.status(418).entity(e.getMessage()).build();
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
				
				try {
					StatisticsDAO statDao = new StatisticsDAO();
					statDao.save(this.userId, UserActivity.DELETE_ARTICLE);
				} catch (StatisticsDAOException e) {
					return Response.status(400).entity(e.getMessage()).build();
				}
				
				return Response.ok().build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}

		} catch (ArticlesDAOException e) {
			logger.error("User with id = " + userId
					+ " failed to delete an article with id = " + id + ".");
			return Response.status(418).build();
		}
	}
}
