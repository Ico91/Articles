package articles.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import articles.web.listener.ConfigurationListener;

public class PersistenceDAO {
	protected static final EntityManagerFactory factory = Persistence
			.createEntityManagerFactory(ConfigurationListener.PERSISTENCE_NAME);
	protected EntityManager entityManager;
	
	public PersistenceDAO() {
		entityManager = factory.createEntityManager();
	}
}
