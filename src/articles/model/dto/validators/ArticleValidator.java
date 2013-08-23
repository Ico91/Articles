package articles.model.dto.validators;

import java.util.ArrayList;
import java.util.List;

import articles.model.Articles.Article;

/**
 * Class used to validate articles
 * 
 * @author Krasimir Atanasov
 * 
 */
//TODO why this class is placed in package dto ? 
public class ArticleValidator {

	public ArticleValidator() {
		// Empty body
	}

	/**
	 * Validate article content and title
	 * 
	 * @param article
	 *            Article to validate
	 * @return List of MessageKeys
	 */
	public List<MessageKeys> validate(Article article) {
		List<MessageKeys> listOfMessageKeys = new ArrayList<MessageKeys>();

		if (article.getTitle() == null)
			listOfMessageKeys.add(MessageKeys.TITLE_IS_NULL);
		else if (article.getTitle().length() == 0)
			listOfMessageKeys.add(MessageKeys.TITLE_IS_EMPTY);

		if (article.getContent() == null)
			listOfMessageKeys.add(MessageKeys.CONTENT_IS_NULL);
		else if (article.getContent().length() == 0)
			listOfMessageKeys.add(MessageKeys.CONTENT_IS_EMPTY);

		return listOfMessageKeys;
	}

	/**
	 * Check if article title is unique
	 * 
	 * @param article
	 *            Article to check
	 * @param listOfArticles
	 * @return True if article title is unique, false otherwise
	 */
	public boolean uniqueTitle(Article article, List<Article> listOfArticles) {
		for (Article a : listOfArticles) {
			if (a.getTitle().equals(article.getTitle())) {
				return false;
			}
		}

		return true;
	}
}
