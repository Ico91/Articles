package articles.web.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import articles.dao.ArticlesDAO;
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
	public Article getArticle(@PathParam("id") int id) {
		for (Article art : listOfArticles) {
			if (art.getId() == id) {
				return art;
			}
		}

		return new Article();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateArticle(Article article) {

		for (int i = 0; i < this.listOfArticles.size(); i++) {
			if (this.listOfArticles.get(i).getId() == article.getId()) {
				this.listOfArticles.set(i, article);
				break;
			}
		}

		this.dao.saveArticles(this.userId, this.listOfArticles);
	}

	@DELETE
	public void deleteArticle(@PathParam("id") int id) {
		for (int i = 0; i < this.listOfArticles.size(); i++) {
			if (this.listOfArticles.get(i).getId() == id) {
				this.listOfArticles.remove(i);
				break;
			}
		}

		this.dao.saveArticles(this.userId, this.listOfArticles);
	}
}
