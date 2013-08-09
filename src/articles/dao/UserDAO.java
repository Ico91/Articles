package articles.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import articles.model.User;

public class UserDAO {
	private static final String PERSISTENCE_UNIT_NAME = "UserPE";
	private static EntityManagerFactory factory;
	private EntityManager entityManager;
	
	public UserDAO() {
		UserDAO.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		this.entityManager = UserDAO.factory.createEntityManager();
	}
	
	public User find(String username, String password) {
		Query selectUserQuery = this.entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password");
		selectUserQuery.setParameter("username", username);
		selectUserQuery.setParameter("password", password);
		User user = new User();
		try {
			user = (User) selectUserQuery.getSingleResult();
			
		} catch (NoResultException nre) {
			nre.printStackTrace();
		}
		
		return user;
	}
	
	public void updateLastLogin(Date lastLogin, int userId) {
		EntityTransaction trans = entityManager.getTransaction();
		trans.begin();
		Query updateLastLoginQuery = this.entityManager.createQuery("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userId = :userId");
		updateLastLoginQuery.setParameter("lastLogin", lastLogin);
		updateLastLoginQuery.setParameter("userId", userId);
		int updated = updateLastLoginQuery.executeUpdate ();
		trans.commit();
		//return updated;
	}
	
	
}
