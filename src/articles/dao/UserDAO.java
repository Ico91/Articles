package articles.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import articles.model.User;

/**
 * Object that provides access for manipulation of users data
 * @author Galina Hristova
 *
 */
public class UserDAO {
	private static final String PERSISTENCE_UNIT_NAME = "UserPE";
	private static EntityManagerFactory factory;
	private EntityManager entityManager;

	/**
	 * Class constructor.
	 */
	public UserDAO() {
		UserDAO.factory = Persistence
				.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		this.entityManager = UserDAO.factory.createEntityManager();
	}

	
	/**
	 * Searches if a user with the entered username and password exists and returns it as a result. Otherwise returns as a result null.
	 * @param username
	 * @param password
	 * @return
	 */
	public User getUser(String username, String password) {
		Query selectUserQuery = this.entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password");
		selectUserQuery.setParameter("username", username);
		selectUserQuery.setParameter("password", password);
		 try {
			 User user = (User) selectUserQuery.getSingleResult();
			 return user;
		} catch (NoResultException nre) {
			return null;
		}
	}

	/**
	 * Updates the date when the user with the specific ID last logged in.
	 * @param lastLogin
	 * @param userId
	 */
	public void updateLastLogin(Date lastLogin, int userId) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			entityTransaction.begin();
			Query updateLastLoginQuery = this.entityManager.createQuery("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userId = :userId");
			updateLastLoginQuery.setParameter("lastLogin", lastLogin);
			updateLastLoginQuery.setParameter("userId", userId);
			updateLastLoginQuery.executeUpdate();
		} catch (PersistenceException e) {
			entityTransaction.rollback();
		} finally {
			entityTransaction.commit();
		}
	}

}
