package articles.database.transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import articles.web.listener.ConfigurationListener;

public class PersistenceManager {
	protected static final EntityManagerFactory factory = Persistence
			.createEntityManagerFactory(ConfigurationListener.PERSISTENCE_NAME);
	protected static final EntityManagerFactory testFactory = Persistence
			.createEntityManagerFactory(ConfigurationListener.PERSISTENCE_NAME_TEST);
	protected EntityManager entityManager;

	public void initManager() {
		entityManager = factory.createEntityManager();
	}
	
	public void initTestManager() {
		entityManager = testFactory.createEntityManager();
	}
}
