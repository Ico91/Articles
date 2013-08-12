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

import articles.dao.ArticlesDAO;
import articles.dao.exceptions.ArticlesDAOException;
import articles.model.Articles.Article;

public class ArticleSubResource {
	private int userId;
	private List<Article> listOfArticles;
	private ArticlesDAO dao;
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
				return Response.ok(art, MediaType.APPLICATION_JSON).build();
			}
		}

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
					return Response.status(418).build();
				}
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
					return Response.status(418).build();
				}
				return Response.ok().build();
			}
		}
		
		return Response.status(Status.NOT_FOUND).build();
	}
}
