package articles.web.resources.articles;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import articles.dao.ArticlesDAO;
import articles.dao.StatisticsStorage;
import articles.dao.exceptions.ArticlesDAOException;
import articles.dao.exceptions.StatisticsDAOException;
import articles.model.Articles.Article;
import articles.model.statistics.UserActivity;
import articles.web.listener.ConfigurationListener;
import articles.web.resources.exception.ArticlesResourceException;

/**
 * Class used to process article requests
 * 
 * @author Krasimir Atanasov
 * 
 */
@Path("")
public class ArticlesResource {
	static final Logger logger = Logger.getLogger(ArticlesResource.class);
	@Context
	ServletContext context;
	@Context
	HttpServletRequest servletRequest;

	private List<Article> articles;
	private ArticlesDAO dao;

	/**
	 * If searchTerm is specified, returns list of articles
	 * containing the searchTerm in title or content, else return
	 * list of all articles
	 * @param searchTerm 
	 * @return List of articles
	 * @throws ArticlesResourceException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Article> getArticles(@QueryParam("search") String searchTerm)
			throws ArticlesResourceException {
		initArticlesDAO();
		try {
			this.articles = this.dao.loadArticles(getUserId());
			if (searchTerm == null) {
				return this.articles;
			} else {
				return search(searchTerm, this.articles);
			}
		} catch (ArticlesDAOException e) {
			return new ArrayList<Article>();
		}
	}
	
	/**
	 * Add new article in already existing list of articles
	 * Returns response code: 200 on success, 400 on fail
	 * @param article Article to add
	 * @return Added article with generated unique article ID
	 * @throws ArticlesResourceException
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(Article article) throws ArticlesResourceException {
		initArticlesDAO();
		try {
			article = this.dao.addArticle(getUserId(), article);
			logger.info("User with id = " + getUserId()
					+ " created an article.");
			
			try {
				StatisticsStorage statDao = new StatisticsStorage();
				statDao.save(getUserId(), UserActivity.CREATE_ARTICLE);
			} catch (StatisticsDAOException e) {
				return Response.status(400).entity(e.getMessage()).build();
			}
			
			return Response.ok(article, MediaType.APPLICATION_JSON).build();

		} catch (ArticlesDAOException e) {
			logger.error("User with id = " + getUserId()
					+ " failed to create an article.");
			return Response.status(400).build();
		}
	}
	
	/**
	 * Process all request on path /articles/{id}
	 * @param id Id of article
	 * @return ArticleSubResource
	 * @throws ArticlesResourceException
	 */
	@Path("{id}")
	public ArticleSubResource getArticle(@PathParam("id") int id)
			throws ArticlesResourceException {
		initArticlesDAO();
		return new ArticleSubResource(this.dao, getUserId());
	}
	
	/**
	 * Read the articles directory from configuration file
	 * @return
	 * @throws ArticlesResourceException
	 */
	private String articlesPath() throws ArticlesResourceException {
		String s = ConfigurationListener.getPath();
		return s;
	}

	/**
	 * Search for searchTerm in title and content of articles 
	 * @param searchTerm String to search
	 * @param listOfArticles List of articles that seek
	 * @return List of articles that contains the searchTerm
	 * @throws ArticlesResourceException
	 */
	private List<Article> search(String searchTerm, List<Article> listOfArticles)
			throws ArticlesResourceException {
		List<Article> articlesToReturn = new ArrayList<Article>();

		for (Article a : listOfArticles) {
			String articleTitle = a.getTitle();
			String articleContent = a.getContent();

			if (articleTitle.contains(searchTerm)
					|| articleContent.contains(searchTerm))
				articlesToReturn.add(a);
		}

		return articlesToReturn;
	}
	
	/**
	 * Get user ID from session
	 * @return User ID
	 */
	private int getUserId() {
		return (int) servletRequest.getSession().getAttribute("userId");
	}

	/**
	 * Initialize ArticlesDAO object
	 * @throws ArticlesResourceException
	 */
	private void initArticlesDAO() throws ArticlesResourceException {
		this.dao = new ArticlesDAO(articlesPath());
	}
}
