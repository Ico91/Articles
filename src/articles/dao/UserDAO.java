package articles.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.dao.exceptions.PersistenceDAOException;
import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.statistics.UserActivity;
import articles.model.statistics.UserStatistics;
import articles.statistics.dto.UserStatisticsDTO;
import articles.web.resources.users.UsersResource;

/**
 * Provides access for manipulation of users data.
 * @author Galina Hristova
 *
 */
public class UserDAO extends PersistenceDAO {
	static final Logger logger = Logger.getLogger(UserDAO.class);
	
	public UserDAO() {
		super();
	}
	
	/**
	 * Searches if a user with the entered username and password exists and returns it as a result. Otherwise returns as a result null.
	 * @param username
	 * @param password
	 * @return
	 */
	public User getUser(final String username, final String password) {
		TransactionManager<User> manager = new TransactionManager<User>();
		User user = (User) manager.execute(new TransactionalTask<User>() {
			@SuppressWarnings("unchecked")
			@Override
			public User executeTask(EntityManager entityManager) throws PersistenceException {
				Query selectUserQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password");
				selectUserQuery.setParameter("username", username);
				selectUserQuery.setParameter("password", password);
				
				List<User> users = (List<User>) selectUserQuery.getResultList();
				
				if (users.size() != 0) {
					return users.get(0);
				} else
					logger.error("User with username = " + username + " and password = " + password + " does not exist.");
					return null;
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
		TransactionManager<Boolean> manager = new TransactionManager<Boolean>();
		boolean res = false;
		if (manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) throws PersistenceException {
				Query updateLastLoginQuery = entityManager.createQuery("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userId = :userId");
				updateLastLoginQuery.setParameter("lastLogin", lastLogin);
				updateLastLoginQuery.setParameter("userId", userId);
				try {
					updateLastLoginQuery.executeUpdate();
				} catch (PersistenceException e) {
					throw new PersistenceDAOException(
							"Error while updating the last login date for user with user id = "
									+ userId);
				}
				//updateLastLoginQuery.executeUpdate();
				
				return Boolean.TRUE;
			}

		}))
			res = true;
		return res;
	}

}
