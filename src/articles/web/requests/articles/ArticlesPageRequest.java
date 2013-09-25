package articles.web.requests.articles;

import java.util.List;

import articles.builders.ArticlesPageBuilder;
import articles.dao.ArticlesDAO;
import articles.model.Articles.Article;
import articles.web.requests.PageRequest;

public class ArticlesPageRequest extends PageRequest {
	private boolean allUsers;
	private int userId;
	private ArticlesDAO articlesDao;
	private String searchTerm;
	private List<Integer> userIds;

	public ArticlesPageRequest(int from, int to, int userId, String path,
			List<Integer> userIds) {
		super(from, to);
		this.allUsers = false;
		this.searchTerm = null;
		this.articlesDao = new ArticlesDAO(path);
		this.userIds = userIds;
		this.userId = userId;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public void setAllUsers(boolean allUsers) {
		this.allUsers = allUsers;
	}

	@Override
	protected Object doProcess() {
		List<Article> listOfArticles = (allUsers) ? articlesDao.loadArticles(
				userIds, searchTerm) : articlesDao.loadUserArticles(userId,
				searchTerm);

		return new ArticlesPageBuilder().buildResult(listOfArticles, from, to);
	}

}
