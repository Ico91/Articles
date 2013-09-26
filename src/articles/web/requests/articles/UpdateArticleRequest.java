package articles.web.requests.articles;

import java.util.List;

import articles.dto.MessageDTO;
import articles.messages.RequestMessageKeys;
import articles.model.Articles.Article;

public class UpdateArticleRequest extends ArticleRequest {

	public UpdateArticleRequest(Article dto, String path, List<Integer> userIds) {
		super(dto, path, userIds);
	}

	@Override
	protected Object processEntity(Article entity) {
		MessageDTO dto = new MessageDTO();
		dto.addMessage(RequestMessageKeys.ARTICLE_UPDATED.getValue());
		
		return (this.articlesDAO.updateArticleFromAllUserArticles(entity,
				userIds)) ? dto : null;
	}

}
