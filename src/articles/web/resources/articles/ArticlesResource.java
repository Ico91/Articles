package articles.web.resources.articles;

import java.util.ArrayList;
import java.util.List;

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
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import articles.dao.ArticlesDAO;
import articles.dao.exceptions.ArticlesDAOException;
import articles.model.Articles.Article;
import articles.model.dto.ArticleIdDTO;
import articles.web.listener.SessionPathConfigurationListener;
import articles.web.resources.exception.ArticlesResourceException;

@Path("")
public class ArticlesResource {
	static final Logger logger = Logger.getLogger(ArticlesResource.class);
	@Context
	ServletContext context;
	@Context
	HttpServletRequest servletRequest;

	private List<Article> articles;
	private ArticlesDAO dao;

	/*public ArticlesResource() throws ArticlesDAOException {
		
	}*/
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Article> getArticles(@QueryParam("search") String searchTerm)
			throws ArticlesResourceException, ArticlesDAOException {
		initArticlesDao();
		this.articles = getListOfArticles();
		if (searchTerm == null) {
			
			return this.articles;
		} else {
			return search(searchTerm, this.articles);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Article article) throws ArticlesResourceException {
		initArticlesDao();
		try {
			int articleId = this.dao.addArticle(getUserId(), article);
			logger.info("User with id = " + getUserId() + " created an article.");
			return Response.ok(articleId, MediaType.APPLICATION_JSON).build();

		} catch (ArticlesDAOException e) {
			logger.error("User with id = " + getUserId() + " failed to create an article.");
			return Response.status(400).build();
		}
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteArticles(ArticleIdDTO articleId)
			throws ArticlesResourceException {
		initArticlesDao();
		List<Article> listOfArticles = this.articles;
		for (int i = 0; i < listOfArticles.size(); i++) {
			if (listOfArticles.get(i).getId() == articleId.getId()) {
				listOfArticles.remove(i);
				try {
					this.dao.saveArticles(getUserId(), new ArrayList<Article>());
				} catch (ArticlesDAOException e) {
					logger.error("User with id = " + getUserId() + " failed to delete all articles.");
					return Response.status(418).build();
				}
				logger.info("User with id = " + getUserId() + " deleted all articles.");
				return Response.ok().build();
			}
		}
		return Response.status(Status.NOT_FOUND).build();
	}

	@Path("{id}")
	public ArticleSubResource getArticle(@PathParam("id") int id)
			throws ArticlesResourceException {
		initArticlesDao();
		return new ArticleSubResource(articlesPath(), getUserId(),
				this.articles);
	}

	private String articlesPath() throws ArticlesResourceException {
		return "E:\\Server\\Articles\\";
		//return SessionPathConfigurationListener.getPath();
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

	private List<Article> getListOfArticles() throws ArticlesDAOException {
		List<Article> articles = null;
		if (this.dao != null) {
			articles = dao.loadArticles(1);
		} else
			System.out.println("null e");

		return articles;
	}
}
