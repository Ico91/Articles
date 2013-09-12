package articles.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import articles.model.User;
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
	public synchronized List<Article> loadUserArticles(int userId) {
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

	// TODO: Implement
	/**
	 * Read all user articles files and return list of articles
	 * 
	 * @return List of articles
	 * @throws DAOException
	 *             when articles file not found, or contains invalid xml
	 */

	public synchronized List<Article> loadArticles() {
		List<Article> listOfArticles = new ArrayList<Article>();
		List<Integer> userIds = getUserIds();

		for (int i : userIds) {
			List<Article> userArticles = loadUserArticles(i);
			if (!userArticles.isEmpty()) {
				listOfArticles.addAll(userArticles);
			}
		}

		return listOfArticles;
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
		File articlesFile = new File(this.articlesPath + "/" + id + ".xml");

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
	public Article getArticleById(String articleId) {
		List<Article> listOfArticles = loadArticles();

		for (Article a : listOfArticles) {
			if (a.getId().equals(articleId))
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
		final List<Article> articles = loadUserArticles(userId);
		validate(new ArticleValidator(article, articles));

		return manager.execute(new TransactionalTask<Articles.Article>() {

			@Override
			public Article executeTask(EntityManager entityManager) {
				article.setId(generateArticleId());
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
	 * Update content or title of article in list of specified user articles
	 * 
	 * @param userId
	 *            User ID
	 * @param article
	 *            Article to update
	 * @return True on success, false otherwise
	 */
	public boolean updateUserArticle(final int userId, final Article article) {
		final List<Article> articles = loadUserArticles(userId);

		if (article.getId() == null) {
			throw new DAOException("Invalid article id");
		}

		validate(new ArticleValidator(article, articles));

		return manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				for (int i = 0; i < articles.size(); i++) {
					if (articles.get(i).getId().equals(article.getId())) {
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
	 * Update content or title of article in list of all user articles
	 * 
	 * @param userId
	 *            User ID
	 * @param article
	 *            Article to update
	 * @return True on success, false otherwise
	 */
	public boolean updateArticleFromAllUserArticles(Article article) {
		List<Integer> ids = getUserIds();

		for (int i : ids) {
			if (updateUserArticle(i, article))
				return true;
		}

		return false;
	}

	/**
	 * Delete article from available user articles
	 * 
	 * @param userId
	 *            User ID
	 * @param articleId
	 *            Article ID
	 * @return True on success, false otherwise
	 */
	public boolean deleteUserArticle(final int userId, final String articleId) {
		final List<Article> listOfArticles = loadUserArticles(userId);

		return manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				for (int i = 0; i < listOfArticles.size(); i++) {
					if (listOfArticles.get(i).getId().equals(articleId)) {
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
	 * Delete article from all user articles
	 * @param articleId
	 * @return True on success, false otherwise
	 */
	public boolean deleteArticle(String articleId) {
		List<Integer> ids = getUserIds();

		for (int i : ids) {
			if (deleteUserArticle(i, articleId))
				return true;
		}

		return false;
	}

	/**
	 * Generate unique article id
	 * 
	 * @param articles
	 * @return Unique ID
	 */
	private String generateArticleId() {
		return UUID.randomUUID().toString();
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

	/**
	 * Get list of user ids
	 * 
	 * @return List of user ids
	 */
	private List<Integer> getUserIds() {
		UserDAO userDAO = new UserDAO();
		List<Integer> userIds = new ArrayList<Integer>();
		List<User> users = userDAO.getUsers();

		for (User u : users) {
			userIds.add(u.getUserId());
		}

		return userIds;
	}
}
