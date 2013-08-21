package articles.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.model.User;
import articles.web.resources.users.UsersResource;

/**
 * Provides access for manipulation of users data.
 * @author Galina Hristova
 *
 */
public class UserDAO extends PersistenceDAO {
	static final Logger logger = Logger.getLogger(UserDAO.class);
	
	public UserDAO() {
		this.entityManager = UserDAO.factory.createEntityManager();
	}
	
	/**
	 * Searches if a user with the entered username and password exists and returns it as a result. Otherwise returns as a result null.
	 * @param username
	 * @param password
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public User getUser(String username, String password) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			entityTransaction.begin();
			entityTransaction.setRollbackOnly();
			Query selectUserQuery = this.entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password");
			selectUserQuery.setParameter("username", username);
			selectUserQuery.setParameter("password", password);
			
			List<User> users = (List<User>) selectUserQuery.getResultList();
			entityTransaction.rollback();
			
			if (users.size() != 0) {
				return users.get(0);
			} else
				logger.error("User with username = " + username + " and password = " + password + " does not exist.");
				return null;
		} finally {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
		}
	}

	/**
	 * Updates the date when the user with the specific ID last logged in.
	 * @param lastLogin
	 * @param userId
	 */
	public boolean updateLastLogin(Date lastLogin, int userId) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			entityTransaction.begin();
			Query updateLastLoginQuery = this.entityManager.createQuery("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userId = :userId");
			updateLastLoginQuery.setParameter("lastLogin", lastLogin);
			updateLastLoginQuery.setParameter("userId", userId);
			updateLastLoginQuery.executeUpdate();
			entityTransaction.commit();
			logger.info("The last login date for user with id = " + userId + " is updated.");
			return true;
		} catch (PersistenceException e) {
			logger.error("The update query failed for user with id = " + userId + ".");
			return false;
		} finally {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
		}
	}

}
