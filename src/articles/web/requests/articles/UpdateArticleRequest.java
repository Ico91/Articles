package articles.web.requests.articles;

import articles.dto.MessageDTO;
import articles.messages.RequestMessageKeys;
import articles.model.Article;
import articles.model.UserType;

public class UpdateArticleRequest extends ArticleRequest {
	private int userId;
	private UserType userType;

	public UpdateArticleRequest(Article dto, String path, int userId,
			UserType userType) {
		super(dto, path);
		this.userId = userId;
		this.userType = userType;
	}

	@Override
	protected Object processEntity(Article entity) {
		MessageDTO dto = new MessageDTO();
		dto.addMessage(RequestMessageKeys.ARTICLE_UPDATED.getValue());
		boolean result = false;

		if (userType == UserType.ADMIN) {
			result = (this.articlesDAO.articleExist(entity.getId())) ? this.articlesDAO
					.updateArticle(this.userId, entity) : false;
		} else {
			result = (this.articlesDAO.userArticleExist(this.userId,
					entity.getId())) ? this.articlesDAO.updateArticle(
					this.userId, entity) : false;
		}

		return (result) ? dto : null;
	}

}
