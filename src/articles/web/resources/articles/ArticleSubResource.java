package articles.web.resources.articles;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.model.Articles.Article;

/**
 * Class used to process request to specified article
 * 
 * @author Krasimir Atanasov
 * 
 */
public class ArticleSubResource extends ArticlesResourceBase {

	public ArticleSubResource(HttpServletRequest servletRequest) {
		super(servletRequest);
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
		Article article = this.dao.getArticleById(userId, id);

		if (article == null) {
			logger.info("User with id = " + userId
					+ " request article that does not exist.");
			return Response.status(Status.NOT_FOUND).build();

		}

		logger.info("User with id = " + userId
				+ " opened an article with id = " + id + ".");
		return Response.ok(article, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Update article with specified ID If article with specified ID is found -
	 * response code 200, else response code 404.
	 * 
	 * @param article
	 *            Article with updated title and/or content
	 * @param id
	 *            Article ID
	 * @return Response containing the response code
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateArticle(Article article, @PathParam("id") int id) {
		article.setId(id);

		Response validationResponse = validationResponse(article);

		if (validationResponse != null) {
			logger.info("User with id = " + userId
					+ " try to update invalid article");
			return validationResponse;
		}

		if (!this.dao.updateArticle(this.userId, article)) {
			logger.info("User with id = " + userId
					+ " try to update article that does not exist");
			return Response.status(Status.NOT_FOUND).build();
		}

		logger.info("User with id = " + userId
				+ " updated an article with id = " + id + ".");
		return Response.ok().build();

	}

	/**
	 * Delete article with specified ID Returns response code 200 when article
	 * was successfully deleted, response code 404 when article is not found
	 * 
	 * @param id
	 *            Article ID
	 * @return Response with response code
	 */
	@DELETE
	public Response deleteArticle(@PathParam("id") int id) {

		if (!this.dao.deleteArticle(userId, id)) {
			logger.info("User with id = " + userId
					+ " failed to delete article with id " + id);
			return Response.status(Status.NOT_FOUND).build();
		}

		logger.info("User with id = " + userId
				+ " deleted an article with id = " + id + ".");

		return Response.ok().build();
	}
}
