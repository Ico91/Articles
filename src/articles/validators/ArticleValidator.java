package articles.validators;

import java.util.ArrayList;
import java.util.List;

import articles.model.Articles.Article;

/**
 * Class used to validate articles
 * 
 * @author Krasimir Atanasov
 * 
 */
public class ArticleValidator implements Validator{
	private Article article;
	private List<Article> articles;
	
	public ArticleValidator(Article article, List<Article> articles) {
		this.article = article;
		this.articles = articles;
	}

	/**
	 * Validate article content and title
	 * 
	 * @param article
	 *            Article to validate
	 * @return List of MessageKeys
	 */
	@Override
	public List<MessageKey> validate() {
		List<MessageKey> listOfMessageKeys = new ArrayList<MessageKey>();

		if (this.article.getTitle() == null)
			listOfMessageKeys.add(ArticleErrorMessageKeys.TITLE_IS_NULL);
		else if (this.article.getTitle().length() == 0)
			listOfMessageKeys.add(ArticleErrorMessageKeys.TITLE_IS_EMPTY);

		if (this.article.getContent() == null)
			listOfMessageKeys.add(ArticleErrorMessageKeys.CONTENT_IS_NULL);
		else if (this.article.getContent().length() == 0)
			listOfMessageKeys.add(ArticleErrorMessageKeys.CONTENT_IS_EMPTY);

		if (!uniqueTitle())
			listOfMessageKeys.add(ArticleErrorMessageKeys.NOT_UNIQUE_TITLE);

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
	private boolean uniqueTitle() {
		for (Article a : this.articles) {
			if (a.getTitle().equals(this.article.getTitle())) {
				if (!a.equals(this.article)) {
					return false;
				}
			}
		}

		return true;
	}
}
