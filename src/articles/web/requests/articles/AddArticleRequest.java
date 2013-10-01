package articles.web.requests.articles;

import articles.model.Article;

public class AddArticleRequest extends ArticleRequest {
	private int userId;
	
	public AddArticleRequest(Article dto, String path, int userId) {
		super(dto, path);
		this.userId = userId;
	}

	@Override
	protected Object processEntity(Article entity) {
		return this.articlesDAO.addArticle(entity, userId);
	}
}
