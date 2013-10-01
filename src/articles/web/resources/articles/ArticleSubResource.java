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

import articles.dto.MessageDTO;
import articles.messages.RequestMessageKeys;
import articles.model.Article;
import articles.model.UserType;
import articles.web.listener.ConfigurationListener;
import articles.web.requests.articles.UpdateArticleRequest;

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
		Article article = this.articlesDao.getArticleById(id);
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
	public Response updateArticle(Article article,
			@PathParam("id") final String articleId) {
		UserType userType = (UserType) servletRequest.getSession()
				.getAttribute(ConfigurationListener.USERTYPE);
		article.setId(articleId);

		if (articlesDao.getArticleById(articleId) == null) {
			logger.info("User with id = " + userId
					+ " try to update article that does not exist");
			return Response.status(Status.NOT_FOUND).build();
		}

		if (userType != UserType.ADMIN) {
			if (articlesDao.getUserArticleById(userId, articleId) == null) {
				logger.info("User with id = " + userId
						+ " try to update other user's article");
				return Response.status(Status.FORBIDDEN).build();
			}
		}

		return new UpdateArticleRequest(article,
				ConfigurationListener.getPath(), userId, userType).process();
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteArticle(@PathParam("id") String id) {
		UserType userType = (UserType) servletRequest.getSession()
				.getAttribute(ConfigurationListener.USERTYPE);
		boolean result = false;

		if (articlesDao.getArticleById(id) == null) {
			logger.info("User with id = " + userId
					+ " try to update article that does not exist");
			return Response.status(Status.NOT_FOUND).build();
		}

		if (userType == UserType.ADMIN) {
			result = this.articlesDao.deleteArticle(id, userId);
		} else {
			result = (articlesDao.userArticleExist(userId, id)) ? this.articlesDao
					.deleteArticle(id, userId) : false;
		}

		if (!result) {
			logger.info("User with id = " + userId
					+ " failed to delete article with id " + id);
			return Response.status(Status.FORBIDDEN).build();
		}

		logger.info("User with id = " + userId
				+ " deleted an article with id = " + id + ".");

		MessageDTO dto = new MessageDTO();
		dto.addMessage(RequestMessageKeys.ARTICLE_DELETED.getValue());
		return Response.ok(dto, MediaType.APPLICATION_JSON).build();
	}
}
