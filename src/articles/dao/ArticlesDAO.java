package articles.dao;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import articles.dao.exceptions.ArticlesDAOException;
import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.model.Articles;
import articles.model.Articles.Article;
import articles.model.dto.validators.ArticleValidator;
import articles.model.dto.validators.MessageBuilder;
import articles.model.dto.validators.MessageKeys;
import articles.model.statistics.UserActivity;

/**
 * Provides access to article files
 * 
 * @author Krasimir Atanasov
 * 
 */
public class ArticlesDAO {
	private String articlesPath;
	private ArticleValidator articleValidator;
	static final Logger logger = Logger.getLogger(ArticlesDAO.class);

	/**
	 * Constructs new ArticlesDAO object
	 * 
	 * @param articlesPath
	 *            Path to articles file
	 */
	public ArticlesDAO(String articlesPath) {
		this.articlesPath = articlesPath;
		this.articleValidator = new ArticleValidator();
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
		Articles articles;
		try {
			File file = new File(pathToArticlesFile(userId));
			if (file.exists()) {
				JAXBContext jaxbContext = JAXBContext
						.newInstance(Articles.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext
						.createUnmarshaller();
				articles = (Articles) jaxbUnmarshaller.unmarshal(file);
			} else {
				logger.error("Articles file does not exist.");
				throw new ArticlesDAOException("Articles file not found!");
			}

		} catch (JAXBException e) {
			String message = "Invalid articles file format!";
			logger.error(message);
			throw new ArticlesDAOException(message);
		}

		return articles.getArticle();
	}

	/**
	 * Get article with specified ID. Returns null if article not exist
	 * 
	 * @param userId
	 *            User ID
	 * @param articleId
	 *            ID of requested article
	 * @return Requested article
	 * @throws ArticlesDAOException
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
	 * @throws ArticlesDAOException
	 */
	public Article addArticle(final int userId, final Article article) {
		final List<Article> articles = loadArticles(userId);
		validateArticle(article);

		if (this.articleValidator.uniqueTitle(article, articles) == false) {
			logger.error("User with id " + userId
					+ " failed to add article. Reason: title not unique");
			throw new ArticlesDAOException("Article title must be unique");
		}

		return transaction(new TransactionalTask<Articles.Article>() {

			@Override
			public Article executeTask(EntityManager entityManager) {
				article.setId(generateArticleId(articles));
				articles.add(article);

				StatisticsStorage storage = new StatisticsStorage(entityManager);
				storage.save(userId, UserActivity.CREATE_ARTICLE);

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
	 * @throws ArticlesDAOException
	 */
	public boolean updateArticle(final int userId, final Article article) {
		final List<Article> articles = loadArticles(userId);

		validateArticle(article);
		return transaction(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				for (int i = 0; i < articles.size(); i++) {
					if (articles.get(i).getId() == article.getId()) {
						articles.remove(i);
						articles.add(i, article);

						StatisticsStorage storage = new StatisticsStorage(
								entityManager);
						storage.save(userId, UserActivity.MODIFY_ARTICLE);

						logger.info("User with id " + userId + " updated article with id " + articles.get(i).getId());
						saveArticles(userId, articles);

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
	 * @throws ArticlesDAOException
	 */
	public boolean deleteArticle(final int userId, final int articleId) {
		final List<Article> listOfArticles = loadArticles(userId);

		return transaction(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				for (int i = 0; i < listOfArticles.size(); i++) {
					if (listOfArticles.get(i).getId() == articleId) {
						listOfArticles.remove(i);

						StatisticsStorage storage = new StatisticsStorage(
								entityManager);
						storage.save(userId, UserActivity.DELETE_ARTICLE);

						logger.info("User with id =" + userId
								+ " deleted article with id = " + articleId);
						saveArticles(userId, listOfArticles);

						return true;
					}
				}

				return false;

			}
		});
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
			String message = "Invalid articles file format.";
			logger.error(message);
			throw new ArticlesDAOException(message);
		}
	}

	/**
	 * Validate article format
	 * 
	 * @param article
	 *            Article to validate
	 */
	private void validateArticle(Article article) {
		List<MessageKeys> messageKeys = this.articleValidator.validate(article);

		if (messageKeys.size() != 0) {
			MessageBuilder builder = new MessageBuilder(messageKeys);
			logger.error(builder.getMessage());

			throw new ArticlesDAOException(builder.getMessage());
		}
	}
	
	private <T> T transaction(TransactionalTask<T> task) {
		TransactionManager<T> transactionManager = new TransactionManager<T>();
		return transactionManager.execute(task);
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
}
