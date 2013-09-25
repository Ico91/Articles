package articles.web.requests.articles;

import java.util.List;

import articles.dto.MessageDTO;
import articles.model.Articles.Article;

public class UpdateArticleRequest extends ArticleRequest {

	public UpdateArticleRequest(Article dto, String path, List<Integer> userIds) {
		super(dto, path, userIds);
	}

	@Override
	protected Object processEntity(Article entity) {
		return (this.articlesDAO.updateArticleFromAllUserArticles(entity,
				userIds)) ? new MessageDTO("Article updated") : null;
	}

}
