package articles.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.dao.exceptions.DAOException;
import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.UserActivity;

/**
 * Provides access for manipulation of users data.
 * @author Galina Hristova
 *
 */
public class UserDAO extends DAOBase {	
	private static final String LOGIN_QUERY = 
			"SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
	private static final String UPDATE_QUERY = "UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userId = :userId";
	private static final String NOT_FOUND = "No user found.";
	private static final String MULTIPLE_RESULTS = "More than one users found for specified username.";
	private static final String TRANSACTION_ERROR = "Problem occurs in the transaction.";
	
	/**
	 * Searches if a user with the entered username and password exists and returns it as a result.
	 * Otherwise returns as a result null.
	 * @param username
	 * @param password
	 * @return
	 */
	
	public UserDAO() {
		logger = Logger.getLogger(getClass());
	}
	
	public User login(final String username, final String password, final UserActivity userActivity,  final Date loginDate) {
		
		User user = manager.execute(new TransactionalTask<User>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public User executeTask(EntityManager entityManager) throws PersistenceException {
				
				Query selectUserQuery = entityManager.createQuery( LOGIN_QUERY );
				selectUserQuery.setParameter("username", username);
				selectUserQuery.setParameter("password", password);
				
				List<User> users = (List<User>) selectUserQuery.getResultList();
				
				if (users.isEmpty()) {
					logger.info(NOT_FOUND);
					return null;
				}
				
				if ( users.size() > 1 ) {
					logger.info(MULTIPLE_RESULTS);
					throw new DAOException(MULTIPLE_RESULTS);
				}
				
				User user = users.get(0);
				int userId = user.getUserId();
				
				addToStatistics(userId, entityManager, userActivity);
				updateLastLogin(userId, loginDate, entityManager);
				
				return user;
			}

		});
		return user;
	}
	
	/**
	 * Updates the date when the user with the specific ID last logged in.
	 * Returns true on success, otherwise throws an exception.
	 * @param lastLogin
	 * @param userId
	 */
	
	private boolean updateLastLogin(final int userId, final Date loginDate, final EntityManager entityManager) {
			Query updateLastLoginQuery = entityManager.createQuery(UPDATE_QUERY);
			updateLastLoginQuery.setParameter("lastLogin", loginDate);
			updateLastLoginQuery.setParameter("userId", userId);
			
			try {
				updateLastLoginQuery.executeUpdate();
				return true;
			} catch (PersistenceException e) {
				logger.error("Error while updating the last login date for user with user id = "
								+ userId + ".");
				throw new DAOException(TRANSACTION_ERROR);
			}
	}
	
	public boolean exitUser(final int userId, final UserActivity userActivity) {
		return manager.execute(new TransactionalTask<Boolean>() {
			
			@Override
			public Boolean executeTask(EntityManager entityManager) throws PersistenceException {
				
				StatisticsStorage statisticsStorage = new StatisticsStorage(entityManager);
				try {
					statisticsStorage.save(userId, userActivity);
					
				} catch(PersistenceException e) {
					logger.error("Error when user with id = " + userId + " logged out.");
					throw new DAOException(TRANSACTION_ERROR);
				}
				
				return true;
			}
		});
	}

}
