package articles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import org.apache.log4j.Logger;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.UserActivity;

/**
 * Provides access for manipulation of users data.
 * 
 * @author Galina Hristova
 * 
 */
public class UserDAO extends DAOBase {

	private static final String LOGIN_QUERY = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
	private static final String UPDATE_USER_QUERY = "UPDATE User u SET u.username = :username, u.password = :password, u.userType = :userType WHERE u.userId = :userId";
	private static final String UPDATE_LASTLOGIN_QUERY = "UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userId = :userId";
	private static final String SELECT_USER_BY_USERNAME = "SELECT u FROM User u WHERE u.username = :username";
	private static final String SELECT_USER_BY_ID = "SELECT u FROM User u WHERE u.userId = :userId";
	private static final String DELETE_QUERY = "DELETE FROM User u WHERE u.userId = :userId";
	private static final String NOT_FOUND = "No user found.";
	private static final String SELECT_ALL_USERS = "SELECT u from User u";

	public UserDAO() {
		logger = Logger.getLogger(getClass());
	}

	public UserDAO(TransactionManager transactionManager) {
		super(transactionManager);
	}

	/**
	 * Searches if a user with the entered username and password exists. Add
	 * statistic information about the user on success.
	 * 
	 * @param username
	 * @param password
	 * @return Existing user or null.
	 */
	public User login(final String username, final String password,
			final UserActivity userActivity, final Date loginDate) {

		return manager.execute(new TransactionalTask<User>() {

			@SuppressWarnings("unchecked")
			@Override
			public User executeTask(EntityManager entityManager)
					throws PersistenceException {

				Query selectUserQuery = entityManager.createQuery(LOGIN_QUERY);
				selectUserQuery.setParameter("username", username);
				selectUserQuery.setParameter("password", password);

				List<User> users = (List<User>) selectUserQuery.getResultList();

				if (users.isEmpty()) {
					logger.info(NOT_FOUND);
					return null;
				}

				User user = users.get(0);
				int userId = user.getUserId();

				addToStatistics(userId, entityManager, userActivity);
				updateLastLogin(userId, loginDate, entityManager);

				return user;
			}

		});
	}

	/**
	 * Find a user with specific ID.
	 * 
	 * @param userId
	 * @return user on success, otherwise null
	 */
	public User getUserById(final int userId) {
		return manager.execute(new TransactionalTask<User>() {

			@Override
			public User executeTask(EntityManager entityManager)
					throws PersistenceException {
				return getUser(userId, entityManager);
			}

		});
	}

	/**
	 * Extract all users stored in the database.
	 * 
	 * @return list of users
	 */
	public List<User> getUsers() {
		return manager.execute(new TransactionalTask<List<User>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<User> executeTask(EntityManager entityManager)
					throws PersistenceException {
				Query selectUsersQuery = entityManager
						.createQuery(SELECT_ALL_USERS);
				List<User> users = (List<User>) selectUsersQuery.getResultList();

				if (users.isEmpty()) {
					logger.info(NOT_FOUND);
					return null;
				}

				return users;
			}
		});
	}

	public List<User> getUsers(final String searchTerm, final int from,
			final int to) {
		return manager.execute(new TransactionalTask<List<User>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<User> executeTask(EntityManager entityManager) {
				CriteriaBuilder qBuilder = entityManager.getCriteriaBuilder();
				CriteriaQuery<User> cQuery = qBuilder.createQuery(User.class);
				EntityType<User> eType = entityManager.getMetamodel().entity(
						User.class);
				Root<User> root = cQuery.from(User.class);

				if (searchTerm != null)
					cQuery.where(qBuilder.like(root.get(eType
							.getDeclaredSingularAttribute("username",
									String.class)), "%" + searchTerm + "%"));

				Query query = entityManager.createQuery(cQuery);
				
				query.setFirstResult(from);
				if (to - from > 0)
					query.setMaxResults(to - from);
				
				return query.getResultList();
			}
		});
	}

	/**
	 * Add new user in the database
	 * 
	 * @param newUser
	 * @return true on success
	 */
	public User addUser(final User newUser) {
		return manager.execute(new TransactionalTask<User>() {
			@Override
			public User executeTask(EntityManager entityManager)
					throws PersistenceException {
				entityManager.persist(newUser);
				
				return getUserByUsername(newUser.getUsername(), entityManager);
			}
		});
	}

	/**
	 * Updates the information about an user with specific ID if that user
	 * exists
	 * 
	 * @param userId
	 * @param user
	 * @return true on success, false if user with the ID does not exist
	 */
	public boolean updateUser(final int userId, final User user) {
		return manager.execute(new TransactionalTask<Boolean>() {
			@Override
			public Boolean executeTask(EntityManager entityManager)
					throws PersistenceException {

				Query updateUserQuery = entityManager
						.createQuery(UPDATE_USER_QUERY);
				updateUserQuery.setParameter("username", user.getUsername());
				updateUserQuery.setParameter("password", user.getPassword());
				updateUserQuery.setParameter("userType", user.getUserType());
				updateUserQuery.setParameter("userId", userId);

				User user = getUser(userId, entityManager);

				if (user == null)
					return false;
				
				updateUserQuery.executeUpdate();
				return true;
				
			}
		});
	}

	/**
	 * Delete an user with specific ID if that user exists
	 * 
	 * @param userId
	 * @return true on success or false if the user does not exist
	 */
	public boolean deleteUser(final int userId) {

		return manager.execute(new TransactionalTask<Boolean>() {
			@Override
			public Boolean executeTask(EntityManager entityManager)
					throws PersistenceException {

				Query updateLastLoginQuery = entityManager
						.createQuery(DELETE_QUERY);
				updateLastLoginQuery.setParameter("userId", userId);

				User user = getUser(userId, entityManager);

				if (user != null) {
					updateLastLoginQuery.executeUpdate();
					return true;
				} else {
					return false;
				}
			}
		});
	}

	/**
	 * Log out a user with specific id and stores statistic information about
	 * the activity.
	 * 
	 * @param userId
	 * @param userActivity
	 * @return true on success
	 */
	public boolean logout(final int userId, final UserActivity userActivity) {
		return manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager)
					throws PersistenceException {

				StatisticsStorage statisticsStorage = new StatisticsStorage(
						entityManager);
				statisticsStorage.save(userId, userActivity);

				return true;
			}
		});
	}

	/**
	 * Get list of all user IDs
	 * 
	 * @return List of user IDs
	 */
	public List<Integer> getListOfUserIds() {
		List<User> users = getUsers();
		List<Integer> userIds = new ArrayList<Integer>();

		for (User u : users)
			userIds.add(u.getUserId());

		return userIds;
	}

	// TODO common things - consider common method
	@SuppressWarnings("unchecked")
	private User getUser(final int userId, EntityManager entityManager) {
		Query selectUserQuery = entityManager.createQuery(SELECT_USER_BY_ID);
		selectUserQuery.setParameter("userId", userId);

		List<User> users = (List<User>) selectUserQuery.getResultList();

		if (users.isEmpty()) {
			logger.info(NOT_FOUND);
			return null;
		}

		return users.get(0);
	}

	public Map<Integer, String> getUsersMap() {
		Map<Integer, String> userMap = new HashMap<Integer, String>();
		List<User> listOfUsers = getUsers();

		for (User u : listOfUsers) {
			userMap.put(u.getUserId(), u.getUsername());
		}

		return userMap;
	}

	@SuppressWarnings("unchecked")
	private User getUserByUsername(String username, EntityManager entityManager) {
		Query selectUserQuery = entityManager
				.createQuery(SELECT_USER_BY_USERNAME);
		selectUserQuery.setParameter("username", username);
		List<User> users = (List<User>) selectUserQuery.getResultList();

		if (users.isEmpty()) {
			logger.info(NOT_FOUND);
			return null;
		}

		return users.get(0);
	}

	private void updateLastLogin(final int userId, final Date loginDate,
			final EntityManager entityManager) {
		Query updateLastLoginQuery = entityManager
				.createQuery(UPDATE_LASTLOGIN_QUERY);
		updateLastLoginQuery.setParameter("lastLogin", loginDate);
		updateLastLoginQuery.setParameter("userId", userId);

		updateLastLoginQuery.executeUpdate();
	}

}
