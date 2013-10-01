package articles.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.database.transactions.TransactionalTask;
import articles.model.ArticleInfo;

public class ArticlesInfoDAO extends DAOBase {
	
	private static final String SELECT_INFO_BY_ID = "SELECT i FROM ArticleInfo i WHERE i.userId = :userId";
	private static final String SELECT_BY_ARTICLE_ID = "SELECT i FROM ArticleInfo i WHERE i.articleId = :articleId";
	private static final String SELECT_BY_USER_AND_ARTICLE_ID = "SELECT i FROM ArticleInfo i "
			+ "WHERE i.articleId = :articleId AND i.userId = :userId";
	private static final String SELECT_ALL = "SELECT i FROM ArticleInfo i";
	private static final String DELETE_INFO = "DELETE FROM ArticleInfo i WHERE i.articleId = :articleId";
	
	public ArticlesInfoDAO() {
		this.logger = Logger.getLogger(getClass());
	}
	
	public void addArticleInfo(final ArticleInfo articleInfo) {
		this.manager.execute(new TransactionalTask<ArticleInfo>() {

			@Override
			public ArticleInfo executeTask(EntityManager entityManager) {
				entityManager.persist(articleInfo);
				return articleInfo;
			}
		});
	}
	
	public List<ArticleInfo> getArticlesInfoByUserId(final int userId) {
		return this.manager.execute(new TransactionalTask<List<ArticleInfo>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<ArticleInfo> executeTask(EntityManager entityManager) {
				Query selectQuery = entityManager.createQuery(SELECT_INFO_BY_ID);
				selectQuery.setParameter("userId", userId);
				
				return selectQuery.getResultList();
			}
		});
	}
	
	public List<ArticleInfo> getArticlesInfo() {
		return this.manager.execute(new TransactionalTask<List<ArticleInfo>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<ArticleInfo> executeTask(EntityManager entityManager) {
				Query selectQuery = entityManager.createQuery(SELECT_ALL);
				
				return selectQuery.getResultList();
			}
		});
	}
	
	public Integer deleteArticlesInfo(final String articleId) {
		return this.manager.execute(new TransactionalTask<Integer>() {

			@Override
			public Integer executeTask(EntityManager entityManager) {
				Query deleteQuery = entityManager.createQuery(DELETE_INFO);
				deleteQuery.setParameter("articleId", articleId);
				
				return deleteQuery.executeUpdate();
			}
		});
	}
	
	public Boolean articleExist(final String articleId) {
		return this.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				Query selectQuery = entityManager.createQuery(SELECT_BY_ARTICLE_ID);
				selectQuery.setParameter("articleId", articleId);
				
				return (!selectQuery.getResultList().isEmpty()) ? true : false;
			}
		});
	}
	
	public Boolean userArticleExist(final String articleId, final int userId) {
		return this.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				Query selectQuery = entityManager.createQuery(SELECT_BY_USER_AND_ARTICLE_ID);
				selectQuery.setParameter("articleId", articleId);
				selectQuery.setParameter("userId", userId);
				
				return (!selectQuery.getResultList().isEmpty()) ? true : false;
			}
		});
	}
}
