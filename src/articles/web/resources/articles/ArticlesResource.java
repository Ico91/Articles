package articles.web.resources.articles;

import java.util.ArrayList;
import java.util.List;

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

import articles.builders.ArticlesPageBuilder;
import articles.model.Articles.Article;
import articles.validators.ArticleValidator;
import articles.web.resources.PageRequest;
import articles.web.resources.ResourceRequest;

/**
 * Class used to process article requests
 * 
 * @author Krasimir Atanasov
 * 
 */
@Path("")
public class ArticlesResource extends ArticlesResourceBase {

	public ArticlesResource(@Context HttpServletRequest serlvetRequest) {
		super(serlvetRequest);
	}

	/**
	 * If searchTerm is specified, returns list of articles containing the
	 * searchTerm in title or content, else return list of all articles
	 * 
	 * @param searchTerm
	 * @param all
	 * @return List of articles
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticles(@QueryParam("search") final String searchTerm,
			@QueryParam("all") final boolean allUsers,
			@QueryParam("from") final int from, @QueryParam("to") final int to) {

		return new PageRequest<Article>() {

			@Override
			public Response doProcess(int from, int to) {
				List<Article> listOfArticles = (allUsers) ? dao.loadArticles()
						: dao.loadUserArticles(userId);

				if (searchTerm != null) {
					listOfArticles = search(searchTerm, listOfArticles);
				}

				return Response.ok(
						new ArticlesPageBuilder().buildResult(listOfArticles,
								from, to), MediaType.APPLICATION_JSON).build();
			}

		}.process(from, to);
	}

	/**
	 * Add new article in already existing list of articles Returns response
	 * code: 200 on success, 400 on fail
	 * 
	 * @param article
	 *            Article to add
	 * @return Added article with generated unique article ID
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(Article article) {
		return new ResourceRequest<Article, Article>() {

			@Override
			public Response doProcess(Article article,
					List<Article> listOfArticles) {
				article = dao.addArticle(userId, article);

				logger.info("User with id = " + userId + " created an article.");
				return Response.ok(article, MediaType.APPLICATION_JSON).build();
			}
		}.process(article, this.articles, new ArticleValidator(article,
				articles));

	}

	/**
	 * Process all request on path /articles/{id}
	 * 
	 * @param id
	 *            Id of article
	 * @return ArticleSubResource
	 * @throws ArticlesResourceException
	 */
	@Path("{id}")
	public ArticleSubResource getArticle(@PathParam("id") String id) {
		return new ArticleSubResource(this.servletRequest);
	}

	/**
	 * Search for searchTerm in title and content of articles
	 * 
	 * @param searchTerm
	 *            String to search
	 * @param listOfArticles
	 *            List of articles that seek
	 * @return List of articles that contains the searchTerm
	 * @throws ArticlesResourceException
	 */
	private List<Article> search(String searchTerm, List<Article> listOfArticles) {
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
}
