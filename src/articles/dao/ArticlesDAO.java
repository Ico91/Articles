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
import articles.model.Article;
import articles.model.ArticleInfo;
import articles.model.UserActivity;

/**
 * Provides access to article files
 * 
 * @author Krasimir Atanasov
 * 
 */
public class ArticlesDAO extends DAOBase {

	private String articlesPath;
	private ArticlesInfoDAO articleInfoDAO;

	/**
	 * Constructs new ArticlesDAO object
	 * 
	 * @param articlesPath
	 *            Path to articles file
	 */
	public ArticlesDAO(String articlesPath) {
		this.logger = Logger.getLogger(getClass());
		this.articlesPath = articlesPath;
		this.articleInfoDAO = new ArticlesInfoDAO();
	}

	public Article addArticle(final Article article, final int userId) {
		article.setId(generateId());

		return this.manager.execute(new TransactionalTask<Article>() {

			@Override
			public Article executeTask(EntityManager entityManager) {
				saveArticle(article);
				addToStatistics(userId, entityManager,
						UserActivity.CREATE_ARTICLE);
				articleInfoDAO.addArticleInfo(new ArticleInfo(userId, article
						.getId()));

				return article;

			}
		});
	}
	
	private void saveArticle(Article article) {
		File articleFile = new File(getArticlePath(article.getId()));
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Article.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.marshal(article, articleFile);

		} catch (JAXBException e) {
			String message = "Failed to save articles to file!";
			logger.error(e.getMessage());
			throw new DAOException(message);
		}
	}

	public Article getArticleById(final String articleId) {
		return this.manager.execute(new TransactionalTask<Article>() {

			@Override
			public Article executeTask(EntityManager entityManager) {
				if(articleInfoDAO.articleExist(articleId)) {
					return loadArticle(articleId);
				}
				
				return null;
			}
		});
	}
	
	public Article getUserArticleById(final int userId, final String articleId) {
		return this.manager.execute(new TransactionalTask<Article>() {

			@Override
			public Article executeTask(EntityManager entityManager) {
				if(articleInfoDAO.userArticleExist(articleId, userId)) {
					return loadArticle(articleId);
				}
				
				return null;
			}
		});
	}
	
	public List<Article> getUserArticles(final int userId) {
		return loadArticles(this.articleInfoDAO.getArticlesInfoByUserId(userId));
	}

	public List<Article> getArticles() {
		return loadArticles(this.articleInfoDAO.getArticlesInfo());
	}

	public Boolean updateArticle(final int userId, final Article article) {
		return this.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				if(articleInfoDAO.articleExist(article.getId())) {
					addToStatistics(userId, entityManager, UserActivity.MODIFY_ARTICLE);
					saveArticle(article);
					return true;
				}
				return false;
			}
		});
	}
	
	public Boolean deleteArticle(final String articleId, final int userId) {
		return this.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				if (articleInfoDAO.articleExist(articleId)) {
					File articleFile = new File(getArticlePath(articleId));

					if (articleFile.exists()) {
						articleFile.delete();
						articleInfoDAO.deleteArticlesInfo(articleId);
						addToStatistics(userId, entityManager,
								UserActivity.DELETE_ARTICLE);

						return true;
					}

				}
				return false;
			}
		});
	}
	
	public List<Article> searchArticles(String searchTerm) {
		return search(loadArticles(articleInfoDAO.getArticlesInfo()), searchTerm);
	}
	
	public List<Article> searchUserArticles(String searchTerm, int userId) {
		return search(loadArticles(articleInfoDAO.getArticlesInfoByUserId(userId)), searchTerm);
	}
	
	public boolean articleExist(String articleId) {
		return this.articleInfoDAO.articleExist(articleId);
	}
	
	public boolean userArticleExist(int userId, String articleId) {
		return this.articleInfoDAO.userArticleExist(articleId, userId);
	}
	
	private List<Article> loadArticles(List<ArticleInfo> listOfArticlesInfo) {
		List<Article> listOfArticles = new ArrayList<Article>();

		for (ArticleInfo articleInfo : listOfArticlesInfo)
			listOfArticles.add(loadArticle(articleInfo.getArticleId()));

		return listOfArticles;
	}

	private Article loadArticle(String id) {
		try {
			File file = new File(getArticlePath(id));
			if (!file.exists()) {
				String message = "Articles file not found at: "
						+ getArticlePath(id);
				logger.error(message);
				throw new DAOException(message);
			}

			JAXBContext jaxbContext = JAXBContext.newInstance(Article.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			return (Article) jaxbUnmarshaller.unmarshal(file);

		} catch (JAXBException e) {
			String message = "Invalid articles file format!";
			logger.error(e.getMessage());
			throw new DAOException(message);
		}
	}

	private List<Article> search(List<Article> listOfArticles, String searchTerm) {
		List<Article> listToReturn = new ArrayList<Article>();
		
		for(Article a : listOfArticles) {
			if(a.getContent().contains(searchTerm) || a.getTitle().contains(searchTerm)) {
				listToReturn.add(a);
			}
		}
		return listToReturn;
	}
	
	private String generateId() {
		return UUID.randomUUID().toString();
	}

	private String getArticlePath(String articleId) {
		return this.articlesPath + "/" + articleId + ".xml";
	}
}
