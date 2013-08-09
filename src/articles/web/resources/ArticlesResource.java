package articles.web.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import articles.dao.ArticlesDAO;
import articles.model.Articles.Article;
import articles.model.dto.ArticleIdDTO;
import articles.web.resources.exception.ArticlesResourceException;

@Path("")
public class ArticlesResource {
	@Context
	ServletContext context;
	@Context
	HttpServletRequest servletRequest;

	private ArticlesDAO dao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Article> getArticles(@QueryParam("search") String searchTerm)
			throws ArticlesResourceException {
		initArticlesDao();
		if (searchTerm == null) {
			return getListOfArticles();
		} else {
			return search(searchTerm, getListOfArticles());
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Article article) throws ArticlesResourceException {
		if (validArticle(article)) {
			if (emptyTitle(article))
				return Response.status(400).entity("Title cannot be empty.")
						.build();
			initArticlesDao();
			List<Article> articles = getListOfArticles();

			article.setId(generateArticleId(articles));
			articles.add(article);
			this.dao.saveArticles(getUserId(), articles);

			return Response.ok(article.getId().toString(),
					MediaType.APPLICATION_JSON).build();
		}

		return Response.status(400)
				.entity("Article title or content not specified!").build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteArticles(ArticleIdDTO articleId)
			throws ArticlesResourceException {
		initArticlesDao();
		List<Article> listOfArticles = getListOfArticles();
		for (int i = 0; i < listOfArticles.size(); i++) {
			if (listOfArticles.get(i).getId() == articleId.getId()) {
				listOfArticles.remove(i);
				break;
			}
		}
		this.dao.saveArticles(getUserId(), new ArrayList<Article>());
	}

	@Path("{id}")
	public ArticleSubResource getArticle(@PathParam("id") int id)
			throws ArticlesResourceException {
		initArticlesDao();
		return new ArticleSubResource(articlesPath(), getUserId(),
				getListOfArticles());
	}

	private int generateArticleId(List<Article> articles) {
		int max = Integer.MIN_VALUE;

		for (Article a : articles) {
			if (a.getId() > max)
				max = a.getId();
		}
		if (max < 1)
			return 1;

		return ++max;
	}

	private String articlesPath() throws ArticlesResourceException {
		String path = "";
		Properties properties = new Properties();
		try {
			properties.load(this.context
					.getResourceAsStream("WEB-INF/config.properties"));
			path = properties.getProperty("path") + "/";
		} catch (IOException e) {
			throw new ArticlesResourceException(
					"Invalid properties file specified");
		}

		return path;
	}

	private List<Article> search(String searchTerm, List<Article> listOfArticles)
			throws ArticlesResourceException {
		initArticlesDao();
		List<Article> articles = listOfArticles;
		List<Article> articlesToReturn = new ArrayList<Article>();

		for (Article a : articles) {
			String articleTitle = a.getTitle();
			String articleContent = a.getContent();

			if (articleTitle.contains(searchTerm)
					|| articleContent.contains(searchTerm))
				articlesToReturn.add(a);
		}

		return articlesToReturn;
	}

	private int getUserId() {
		// TODO: Must return userId attribute from session
		return 1;
	}

	private void initArticlesDao() throws ArticlesResourceException {
		this.dao = new ArticlesDAO(articlesPath());
	}

	private List<Article> getListOfArticles() {
		List<Article> articles = dao.loadArticles(getUserId());

		return articles;
	}

	private boolean emptyTitle(Article article) {
		return (article.getTitle().equals(""));
	}

	private boolean validArticle(Article article) {
		return (article.getContent() != null && article.getTitle() != null);
	}
}
