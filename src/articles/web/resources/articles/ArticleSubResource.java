package articles.web.resources.articles;

import java.util.List;

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

import articles.model.UserType;
import articles.model.Articles.Article;
import articles.validators.ArticleValidator;
import articles.web.listener.ConfigurationListener;
import articles.web.resources.ResourceRequest;

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
	public Response getArticle(@PathParam("id") String id) {
		Article article = this.dao.getArticleById(id);

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
	 * response code 204, else response code 404.
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
	public Response updateArticle(final Article article,
			@PathParam("id") final String articleId) {
		// TODO: Duplicated with DELETE
		if (dao.getArticleById(articleId) == null) {
			logger.info("User with id = " + userId
					+ " try to update article that does not exist");
			return Response.status(Status.NOT_FOUND).build();
		}

		article.setId(articleId);

		return new ResourceRequest<Article, Article>() {

			@Override
			public Response doProcess(Article objectToValidate,
					List<Article> listOfObjects) {

				boolean result = (servletRequest.getSession().getAttribute(
						ConfigurationListener.USERTYPE) == UserType.ADMIN) ? dao
						.updateArticleFromAllUserArticles(article) : dao
						.updateUserArticle(userId, article);

				if (!result) {
					logger.info("User with id = " + userId
							+ " try to update other user's article");
					return Response.status(Status.FORBIDDEN).build();
				}

				logger.info("User with id = " + userId
						+ " updated an article with id = " + articleId + ".");
				return Response.noContent().build();
			}
		}.process(article, this.articles, new ArticleValidator(article,
				articles));
	}

	/**
	 * Delete article with specified ID Returns response code 204 when article
	 * was successfully deleted, response code 404 when article is not found
	 * 
	 * @param id
	 *            Article ID
	 * @return Response with response code
	 */
	@DELETE
	public Response deleteArticle(@PathParam("id") String id) {
		if (dao.getArticleById(id) == null) {
			logger.info("User with id = " + userId
					+ " try to update article that does not exist");
			return Response.status(Status.NOT_FOUND).build();
		}

		boolean result = (servletRequest.getSession().getAttribute(
				ConfigurationListener.USERTYPE) == UserType.ADMIN) ? this.dao
				.deleteArticle(id) : this.dao.deleteUserArticle(userId, id);

		if (!result) {
			logger.info("User with id = " + userId
					+ " failed to delete article with id " + id);
			return Response.status(Status.FORBIDDEN).build();
		}

		logger.info("User with id = " + userId
				+ " deleted an article with id = " + id + ".");

		return Response.noContent().build();
	}
}
