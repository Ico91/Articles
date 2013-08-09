package articles.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
		
		User user = (User) selectUserQuery.getSingleResult();
		
		return user;
	}
	
	public int updateLastLogin(Date lastLogin, int userId) {
		Query updateLastLoginQuery = this.entityManager.createQuery("UPDATE User u SET u.last_login=:lastLogin WHERE u.userid=:userId");
		updateLastLoginQuery.setParameter("lastLogin", lastLogin);
		updateLastLoginQuery.setParameter("userid", userId);
		int updated = updateLastLoginQuery.executeUpdate ();
		return updated;
	}
	
	
}
