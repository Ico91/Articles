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
import articles.web.resources.users.UsersResource;

public class ArticleSubResource {
	private int userId;
	private List<Article> listOfArticles;
	private ArticlesDAO dao;
	static final Logger logger = Logger.getLogger(UsersResource.class);
	// TODO: Change String articlesPath to ArticlesDTO
	public ArticleSubResource(String articlesPath, int userId,
			List<Article> listOfArticles) {
		this.userId = userId;
		this.listOfArticles = listOfArticles;
		this.dao = new ArticlesDAO(articlesPath);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticle(@PathParam("id") int id) {
		for (Article art : listOfArticles) {
			if (art.getId() == id) {
				logger.info("User with id = " + userId + " opened an article with id = " + id + ".");
				return Response.ok(art, MediaType.APPLICATION_JSON).build();
			}
		}
		logger.error("User with id = " + userId + " failed to open an article with id = " + id + ".");
		return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateArticle(Article article, @PathParam("id") int id) {

		for (int i = 0; i < this.listOfArticles.size(); i++) {
			if (this.listOfArticles.get(i).getId() == id) {
				this.listOfArticles.set(i, article);
				try {
					this.dao.saveArticles(this.userId, this.listOfArticles);
				} catch (ArticlesDAOException e) {
					logger.error("User with id = " + userId + " failed to update an article with id = " + id + ".");
					return Response.status(418).build();
				}
				logger.info("User with id = " + userId + " updated an article with id = " + id + ".");
				return Response.ok().build();
			}
		}
		
		return Response.status(Status.NOT_MODIFIED).build();
	}

	@DELETE
	public Response deleteArticle(@PathParam("id") int id) {
		for (int i = 0; i < this.listOfArticles.size(); i++) {
			if (this.listOfArticles.get(i).getId() == id) {
				this.listOfArticles.remove(i);
				try {
					this.dao.saveArticles(this.userId, this.listOfArticles);
				} catch (ArticlesDAOException e) {
					logger.error("User with id = " + userId + " failed to delete an article with id = " + id + ".");
					return Response.status(418).build();
				}
				logger.info("User with id = " + userId + " deleted an article with id = " + id + ".");
				return Response.ok().build();
			}
		}
		
		return Response.status(Status.NOT_FOUND).build();
	}
}
