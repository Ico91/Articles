package articles.dao;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import articles.dao.exceptions.ArticlesDAOException;
import articles.model.Articles;
import articles.model.Articles.Article;

/**
 * Provides access and manipulation to articles file of specified user
 * 
 * @author Krasimir Atanasov
 * 
 */
public class ArticlesDAO {
	private String articlesPath;

	/**
	 * Constructs new ArticlesDAO object
	 * 
	 * @param articlesPath
	 *            Path to articles file
	 */
	public ArticlesDAO(String articlesPath) {
		this.articlesPath = articlesPath;
	}

	/**
	 * Read articles file and returns list of articles
	 * 
	 * @param userId
	 *            User ID
	 * @return List of articles
	 * @throws ArticlesDAOException
	 */
	public List<Article> loadArticles(int userId) throws ArticlesDAOException {
		Articles articles = new Articles();
		try {

			File file = new File(pathToArticlesFile(userId));
			if (file.exists()) {
				JAXBContext jaxbContext = JAXBContext
						.newInstance(Articles.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext
						.createUnmarshaller();
				articles = (Articles) jaxbUnmarshaller.unmarshal(file);
			}

		} catch (JAXBException e) {
			throw new ArticlesDAOException("Invalid articles.xml file format");
		}

		return articles.getArticle();
	}

	/**
	 * Get article with specified ID
	 * 
	 * @param userId
	 *            User ID
	 * @param articleId
	 *            ID of requested article
	 * @return Requested article
	 * @throws ArticlesDAOException
	 */
	public Article getArticle(int userId, int articleId)
			throws ArticlesDAOException {
		List<Article> listOfArticles = loadArticles(userId);

		for (Article a : listOfArticles) {
			if (a.getId() == articleId)
				return a;
		}

		throw new ArticlesDAOException("Article with id " + articleId
				+ " cannot be found");

	}

	/**
	 * Add article to existing list of articles
	 * 
	 * @param userId
	 *            User ID
	 * @param article
	 *            Article to add
	 * @return Added article with his unique ID
	 * @throws ArticlesDAOException
	 */
	public Article addArticle(int userId, Article article)
			throws ArticlesDAOException {
		validArticle(article);
		List<Article> articles = loadArticles(userId);

		if (hasUniqueTitle(article, articles) == false)
			throw new ArticlesDAOException(
					"Article with same title already exist!");

		article.setId(generateArticleId(articles));
		articles.add(article);
		saveArticles(userId, articles);

		return article;
	}

	/**
	 * Update content or title of specified article
	 * 
	 * @param userId
	 *            User ID
	 * @param article
	 *            Article to update
	 * @return True on success, false otherwise
	 * @throws ArticlesDAOException
	 */
	public boolean updateArticle(int userId, Article article)
			throws ArticlesDAOException {
		validArticle(article);
		List<Article> articles = loadArticles(userId);

		for (int i = 0; i < articles.size(); i++) {
			if (articles.get(i).getId() == article.getId()) {
				articles.remove(i);
				articles.add(i, article);
				saveArticles(userId, articles);

				return true;
			}
		}

		return false;
	}

	/**
	 * Delete article from available articles
	 * 
	 * @param userId
	 *            User ID
	 * @param articleId
	 *            Article ID
	 * @return True on success, false otherwise
	 * @throws ArticlesDAOException
	 */
	public boolean deleteArticle(int userId, int articleId)
			throws ArticlesDAOException {
		List<Article> listOfArticles = loadArticles(userId);

		for (int i = 0; i < listOfArticles.size(); i++) {
			if (listOfArticles.get(i).getId() == articleId) {
				listOfArticles.remove(i);
				saveArticles(userId, listOfArticles);

				return true;
			}
		}

		return false;
	}

	/**
	 * Write list of articles in file
	 * 
	 * @param userId
	 *            User ID
	 * @param articlesList
	 *            List of articles to write
	 * @throws ArticlesDAOException
	 */
	public void saveArticles(int userId, List<Article> articlesList)
			throws ArticlesDAOException {
		Articles article = new Articles();
		article.setArticle(articlesList);
		try {

			File file = new File(pathToArticlesFile(userId));
			JAXBContext jaxbContext = JAXBContext.newInstance(Articles.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(article, file);

		} catch (JAXBException e) {
			throw new ArticlesDAOException("Invalid articles format.");
		}
	}

	/**
	 * Check if the article title is unique
	 * 
	 * @param article
	 *            Article to check
	 * @param articles
	 *            List of all articles
	 * @return True if article title is unique, false otherwise
	 */
	private boolean hasUniqueTitle(Article article, List<Article> articles) {
		for (Article a : articles) {
			if (a.getTitle().equals(article.getTitle())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Generate unique article id
	 * 
	 * @param articles
	 * @return Unique ID
	 */
	private int generateArticleId(List<Article> articles) {
		int max = Integer.MIN_VALUE;

		for (Article a : articles) {
			if (a.getId() > max)
				max = a.getId();
		}
		if (max < 1)
			return 1;

		return ++max;
	}

	/**
	 * Check if passed article contains title and content
	 * 
	 * @param article
	 *            Articles to validate
	 * @throws ArticlesDAOException
	 *             On invalid article
	 */
	private void validArticle(Article article) throws ArticlesDAOException {
		if (article.getTitle() == null || article.getTitle().equals(""))
			throw new ArticlesDAOException(
					"Invalid article format. Article must contain title!");
		if (article.getContent() == null)
			throw new ArticlesDAOException(
					"Invalid article format. Article must contain content!");
	}

	/**
	 * Returns path to articles file of the specified user
	 * 
	 * @param userId
	 *            User ID
	 * @return Full path to file
	 */
	private String pathToArticlesFile(int userId) {
		return this.articlesPath + "/" + userId + ".xml";
	}
}
