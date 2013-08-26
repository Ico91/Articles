package articles.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.dao.exceptions.PersistenceDAOException;
import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.statistics.UserActivity;

/**
 * Provides access for manipulation of users data.
 * @author Galina Hristova
 *
 */
public class UserDAO extends DAOBase {	
	private static final String LOGIN_QUERY = 
			"SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
	private static final String UPDATE_QUERY = "UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userId = :userId";
	
	/**
	 * Searches if a user with the entered username and password exists and returns it as a result. Otherwise returns as a result null.
	 * @param username
	 * @param password
	 * @return
	 */
	
	public UserDAO() {
		logger = Logger.getLogger(getClass());
	}
	
	public User login(final String username, final String password, final UserActivity userActivity) {
		
		User user = manager.execute(new TransactionalTask<User>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public User executeTask(EntityManager entityManager) throws PersistenceException {
				
				Query selectUserQuery = entityManager.createQuery( LOGIN_QUERY );
				selectUserQuery.setParameter("username", username);
				selectUserQuery.setParameter("password", password);
				
				List<User> users = (List<User>) selectUserQuery.getResultList();
				
				if (users.isEmpty()) {
					logger.info( "No user found" );
					return null;
				}
				
				//TODO re-factor in separate method
				if ( users.size() > 1 ) {
					throw new PersistenceDAOException( "More than one users found for specified username" );
				}
				
				User user = users.get(0);
				
				addStatistics(userActivity, entityManager, user);
				
				return user;
			}

			//TODO consider void addStatistics( User user, UserActivity userActivity ) {
			
			private void addStatistics(final UserActivity userActivity, EntityManager entityManager, User user) {
				StatisticsStorage statisticsStorage = new StatisticsStorage(entityManager);
				statisticsStorage.save( user.getUserId(), userActivity);
			}
		});
		return user;
	}
	
	/**
	 * Updates the date when the user with the specific ID last logged in.
	 * @param lastLogin
	 * @param userId
	 */
	
	public boolean updateLastLogin(final Date lastLogin, final int userId) {
		return manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				Query updateLastLoginQuery = entityManager.createQuery(UPDATE_QUERY);
				updateLastLoginQuery.setParameter("lastLogin", lastLogin);
				updateLastLoginQuery.setParameter("userId", userId);
				try {
					updateLastLoginQuery.executeUpdate();
					logger.info("Last login date for user with id = " + userId + " is updated.");
					return true;
				} catch (PersistenceException e) {
					throw new PersistenceDAOException(
							"Error while updating the last login date for user with user id = "
									+ userId);
				}
			}

		});
	}
	
	public boolean exitUser(final int userId, final UserActivity userActivity) {
		return manager.execute(new TransactionalTask<Boolean>() {
			
			@Override
			public Boolean executeTask(EntityManager entityManager) throws PersistenceException {
				
				StatisticsStorage statisticsStorage = new StatisticsStorage(entityManager);
				try {
					statisticsStorage.save(userId, userActivity);
					
				} catch(Exception e) {
					throw new PersistenceDAOException(e.getMessage());
				}
				
				return true;
			}
		});
	}

}
