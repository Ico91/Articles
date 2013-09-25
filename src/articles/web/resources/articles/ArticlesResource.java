package articles.web.resources.articles;

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

import articles.model.Articles.Article;
import articles.web.listener.ConfigurationListener;
import articles.web.requests.articles.AddArticleRequest;
import articles.web.requests.articles.ArticlesPageRequest;

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

		ArticlesPageRequest request =  new ArticlesPageRequest(from, to, userId, 
				ConfigurationListener.getPath(), userIds);
		
		request.setSearchTerm(searchTerm);
		request.setAllUsers(allUsers);
		
		return request.process();
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
		return new AddArticleRequest(article, ConfigurationListener.getPath(),
				this.userIds, this.userId).process();
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
}
