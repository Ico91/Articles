package articles.dao;

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
}
