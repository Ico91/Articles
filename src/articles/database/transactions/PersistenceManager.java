package articles.database.transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import articles.web.listener.ConfigurationListener;

/**
 * Base class for creating entityManagers for transactions
 * 
 * @author Galina Hristova
 * 
 */
public class PersistenceManager {
	protected static final EntityManagerFactory factory = Persistence
			.createEntityManagerFactory(ConfigurationListener.PERSISTENCE_NAME);
	protected static final EntityManagerFactory testFactory = Persistence
			.createEntityManagerFactory(ConfigurationListener.PERSISTENCE_NAME_TEST);
	protected EntityManager entityManager;

	/**
	 * Initiate entityManager that manages transactions in the system
	 */
	public void initManager() {
		entityManager = factory.createEntityManager();
	}

	/**
	 * Initiate entityManager that manages test transactions
	 */
	public void initTestManager() {
		entityManager = testFactory.createEntityManager();
	}
}
