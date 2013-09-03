package articles.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import articles.dao.exceptions.DAOException;
import articles.database.transactions.TransactionalTask;
import articles.model.Articles;
import articles.model.Articles.Article;
import articles.model.UserActivity;
import articles.validators.ArticleValidator;

/**
 * Provides access to article files
 * 
 * @author Krasimir Atanasov
 * 
 */
public class ArticlesDAO extends DAOBase {
	private String articlesPath;

	/**
	 * Constructs new ArticlesDAO object
	 * 
	 * @param articlesPath
	 *            Path to articles file
	 */
	public ArticlesDAO(String articlesPath) {
		this.logger = Logger.getLogger(getClass());
		this.articlesPath = articlesPath;
	}

	/**
	 * Read articles file and returns list of articles
	 * 
	 * @param userId
	 *            User ID
	 * @return List of articles
	 * @throws DAOException
	 *             when articles file not found, or contains invalid xml
	 */
	public synchronized List<Article> loadArticles(int userId) {
		Articles articles;
		try {
			File file = new File(pathToArticlesFile(userId));
			if (!file.exists()) {
				String message = "Articles file not found at: "
						+ pathToArticlesFile(userId);
				logger.error(message);
				throw new DAOException(message);
			}

			JAXBContext jaxbContext = JAXBContext.newInstance(Articles.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			articles = (Articles) jaxbUnmarshaller.unmarshal(file);

			return articles.getArticle();

		} catch (JAXBException e) {
			String message = "Invalid articles file format!";
			logger.error(message);
			throw new DAOException(message);
		}
	}

	/**
	 * Write list of articles in file
	 * 
	 * @param userId
	 *            User ID
	 * @param articlesList
	 *            List of articles to write
	 * @throws DAOException
	 */
	public synchronized void saveArticles(int userId, List<Article> articlesList) {
		Articles article = new Articles();
		article.setArticle(articlesList);
		try {

			File file = new File(pathToArticlesFile(userId));
			JAXBContext jaxbContext = JAXBContext.newInstance(Articles.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(article, file);

		} catch (JAXBException e) {
			String message = "Failed to save articles to file!";
			logger.error(message);
			throw new DAOException(message);
		}
	}

	/**
	 * Create empty articles file for user with specified ID
	 * 
	 * @param userId
	 *            User ID
	 */
	public void createUserArticlesFile(int userId) {
		saveArticles(userId, new ArrayList<Article>());
	}

	/**
	 * Delete user articles file
	 * 
	 * @param id
	 *            ID of the user
	 */
	public void deleteUserArticlesFile(int id) {
		File articlesFile = new File(this.articlesPath + "/" + id
				+ ".xml");

		// Synchronization ?
		if (articlesFile.exists()) {
			articlesFile.delete();
		}
	}

	/**
	 * Get article with specified ID. Returns null if article not exist
	 * 
	 * @param userId
	 *            User ID
	 * @param articleId
	 *            ID of requested article
	 * @return Requested article
	 */
	public Article getArticleById(int userId, int articleId) {
		List<Article> listOfArticles = loadArticles(userId);

		for (Article a : listOfArticles) {
			if (a.getId() == articleId)
				return a;
		}

		return null;
	}

	/**
	 * Add article to existing list of articles
	 * 
	 * @param userId
	 *            User ID
	 * @param article
	 *            Article to add
	 * @return Added article with his unique ID
	 */
	public Article addArticle(final int userId, final Article article) {
		final List<Article> articles = loadArticles(userId);
		validate(new ArticleValidator(article, articles));

		return manager.execute(new TransactionalTask<Articles.Article>() {

			@Override
			public Article executeTask(EntityManager entityManager) {
				article.setId(generateArticleId(articles));
				articles.add(article);

				commit(userId, entityManager, articles,
						UserActivity.CREATE_ARTICLE);

				logger.info("User with id " + userId + " created new article.");
				saveArticles(userId, articles);

				return article;
			}
		});
	}

	/**
	 * Update content or title of specified article
	 * 
	 * @param userId
	 *            User ID
	 * @param article
	 *            Article to update
	 * @return True on success, false otherwise
	 */
	public boolean updateArticle(final int userId, final Article article) {
		final List<Article> articles = loadArticles(userId);
		
		if(article.getId() < 1) {
			throw new DAOException("Invalid article id");
		}
		validate(new ArticleValidator(article, articles));

		return manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				for (int i = 0; i < articles.size(); i++) {
					if (articles.get(i).getId() == article.getId()) {
						articles.remove(i);
						articles.add(i, article);

						commit(userId, entityManager, articles,
								UserActivity.MODIFY_ARTICLE);
						logger.info("User with id " + userId
								+ " updated article with id "
								+ articles.get(i).getId());

						return true;
					}
				}

				return false;
			}
		});
	}

	/**
	 * Delete article from available articles
	 * 
	 * @param userId
	 *            User ID
	 * @param articleId
	 *            Article ID
	 * @return True on success, false otherwise
	 */
	public boolean deleteArticle(final int userId, final int articleId) {
		final List<Article> listOfArticles = loadArticles(userId);

		return manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				for (int i = 0; i < listOfArticles.size(); i++) {
					if (listOfArticles.get(i).getId() == articleId) {
						listOfArticles.remove(i);

						commit(userId, entityManager, listOfArticles,
								UserActivity.DELETE_ARTICLE);
						logger.info("User with id =" + userId
								+ " deleted article with id = " + articleId);

						return true;
					}
				}

				return false;
			}
		});
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
	 * Returns path to articles file of the specified user
	 * 
	 * @param userId
	 *            User ID
	 * @return Full path to file
	 */
	private String pathToArticlesFile(int userId) {
		return this.articlesPath + "/" + userId + ".xml";
	}

	// TODO: Better name
	private void commit(int userId, EntityManager entityManager,
			List<Article> listOfArticles, UserActivity activity) {
		addToStatistics(userId, entityManager, activity);
		saveArticles(userId, listOfArticles);
	}
}
