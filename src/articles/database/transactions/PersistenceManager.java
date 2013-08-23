package articles.database.transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import articles.web.listener.ConfigurationListener;

//TODO this name means ?
public class PersistenceManager {
	protected static final EntityManagerFactory factory = Persistence
			.createEntityManagerFactory(ConfigurationListener.PERSISTENCE_NAME);
	protected EntityManager entityManager;
	
	public PersistenceManager() {
		entityManager = factory.createEntityManager();
	}
}
