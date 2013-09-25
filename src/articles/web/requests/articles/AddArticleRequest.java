package articles.web.requests.articles;

import java.util.List;

import articles.model.Articles.Article;

public class AddArticleRequest extends ArticleRequest {
	private int userId;
	
	public AddArticleRequest(Article dto, String path, List<Integer> userIds, int userId) {
		super(dto, path, userIds);
		this.userId = userId;
	}

	@Override
	protected Object processEntity(Article entity) {
		return this.articlesDAO.addArticle(userId, entity);
	}
}
