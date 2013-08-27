package articles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.dao.exceptions.DAOException;
import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.dto.NewUserRequest;
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
	 * Searches if a user with the entered username and password exists.
	 * @param username
	 * @param password
	 * @return Existing user or null.
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
	
	public User getUserById(final int userId) {
		User user = manager.execute(new TransactionalTask<User>() {
			
			@Override
			public User executeTask(EntityManager entityManager) throws PersistenceException {
				return getUser(userId, entityManager);
			}

		});
		
		return user;
	}
	
	public List<User> getUsers() {
		List<User> users = manager.execute(new TransactionalTask<List<User>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<User> executeTask(EntityManager entityManager) throws PersistenceException {
				List<User> users = new ArrayList<User>();
				Query selectUsersQuery = entityManager.createQuery("SELECT u from User u");
				users = (List<User>) selectUsersQuery.getResultList();
				
				if (users.isEmpty()) {
					logger.info(NOT_FOUND);
					return null;
				}
				
				return users;
			}
		});
		
		return users;
	}
	
	public boolean addUser(final NewUserRequest newUser) {
		return manager.execute(new TransactionalTask<Boolean>() {
			@Override
			public Boolean executeTask(EntityManager entityManager) throws PersistenceException {
				
				try {
					User user = new User();
					user.setUsername(newUser.getUsername());
					user.setPassword(newUser.getPassword());
					user.setUserType(newUser.getType());
					entityManager.persist(user);
				} catch (PersistenceException e) {
					logger.error("Error while adding a new user");
					throw new DAOException(TRANSACTION_ERROR);
				}
				return true;
			}
		});
	}
	
	public boolean updateUser(final int userId, final User user) {
		return manager.execute(new TransactionalTask<Boolean>() {
			@Override
			public Boolean executeTask(EntityManager entityManager) throws PersistenceException {
				Query updateUserQuery = entityManager.createQuery("UPDATE User u SET u.username = :username, u.password = :password, u.userType = :userType WHERE u.userId = :userId");
				updateUserQuery.setParameter("username", user.getUsername());
				updateUserQuery.setParameter("password", user.getPassword());
				updateUserQuery.setParameter("userType", user.getUserType());
				updateUserQuery.setParameter("userId", userId);
				User user = getUser(userId, entityManager);
				if (user != null) {
					try {
						updateUserQuery.executeUpdate();
						return true;
					} catch (PersistenceException e) {
						logger.error("Error while updating an user.");
						throw new DAOException(TRANSACTION_ERROR);
					}
				} else {
					return false;
				}
			}
		});
	}
	
	public boolean deleteUser(final int userId) {
		
		return manager.execute(new TransactionalTask<Boolean>() {
			@Override
			public Boolean executeTask(EntityManager entityManager) throws PersistenceException {
				Query updateLastLoginQuery = entityManager.createQuery("DELETE FROM User u WHERE u.userId = :userId");
				updateLastLoginQuery.setParameter("userId", userId);
				User user = getUser(userId, entityManager);
				if (user != null) {
					try {
						updateLastLoginQuery.executeUpdate();
						return true;
					} catch (PersistenceException e) {
						logger.error("Error while deleting an user.");
						throw new DAOException(TRANSACTION_ERROR);
					}
				} else {
					return false;
				}
			}
		});
	}
	
	/**
	 * Logs out a user with specific id and stores statistic information about the activity.
	 * @param userId
	 * @param userActivity
	 * @return true on success
	 */
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

	/**
	 * Updates the date when the user with the specific ID last logged in.
	 * Returns true on success, otherwise throws an exception.
	 * @param lastLogin
	 * @param userId
	 */
	
	@SuppressWarnings("unchecked")
	private User getUser(final int userId, EntityManager entityManager) {
		Query selectUserQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.userId = :userId");
		selectUserQuery.setParameter("userId", userId);
		
		List<User> users = (List<User>) selectUserQuery.getResultList();
		
		if (users.isEmpty()) {
			logger.info(NOT_FOUND);
			return null;
		}
		
		User user = users.get(0);
		
		return user;
	}
	
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
}
