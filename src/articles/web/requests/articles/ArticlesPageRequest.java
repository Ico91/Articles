package articles.web.requests.articles;

import java.util.List;

import articles.builders.ArticlesPageBuilder;
import articles.dao.ArticlesDAO;
import articles.model.Article;
import articles.web.requests.PageRequest;

public class ArticlesPageRequest extends PageRequest {
	private boolean allUsers;
	private int userId;
	private ArticlesDAO articlesDao;
	private String searchTerm;

	public ArticlesPageRequest(int from, int to, int userId, String path) {
		super(from, to);
		this.allUsers = false;
		this.searchTerm = null;
		this.articlesDao = new ArticlesDAO(path);
		this.userId = userId;
	}

	public void setSearchTerm(String searchTerm) {
		if (searchTerm != null)
			this.searchTerm = searchTerm;
		else
			this.searchTerm = "";
	}

	public void setAllUsers(boolean allUsers) {
		this.allUsers = allUsers;
	}

	@Override
	protected Object doProcess() {
		List<Article> listOfArticles = (allUsers) ? articlesDao
				.searchArticles(searchTerm) : articlesDao.searchUserArticles(
				searchTerm, userId);

		return new ArticlesPageBuilder().buildResult(listOfArticles, from, to);
	}

}
