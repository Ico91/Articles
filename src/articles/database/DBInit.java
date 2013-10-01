package articles.database;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import articles.model.ArticleInfo;
import articles.model.User;
import articles.model.UserType;
import articles.web.listener.ConfigurationListener;

public class DBInit {
	protected static final EntityManagerFactory factory = Persistence
			.createEntityManagerFactory(ConfigurationListener.PERSISTENCE_NAME);
	protected static final EntityManager entityManager =  factory.createEntityManager();

	public static void main(String[] args) {
		
		
		
		entityManager.getTransaction().begin();
		/*User user = new User();
		user.setUsername("admin");
		user.setPassword("123");
		user.setLastLogin(Calendar.getInstance().getTime());
		user.setUserId(111);
		user.setUserType(UserType.ADMIN);

		entityManager.persist(user);
		entityManager.getTransaction().commit();

		
		entityManager.getTransaction().begin();
		user = new User();
		user.setUsername("user");
		user.setPassword("321");
		user.setLastLogin(Calendar.getInstance().getTime());
		user.setUserId(112);
		user.setUserType(UserType.USER);

		entityManager.persist(user);
		entityManager.getTransaction().commit();*/

		ArticleInfo articleInfo = new ArticleInfo(1, UUID.randomUUID().toString());
		entityManager.persist(articleInfo);
		
		entityManager.close();
	}
}
