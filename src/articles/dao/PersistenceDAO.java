package articles.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceDAO {
	protected static final String PERSISTENCE_UNIT_NAME = "UserPE";
	protected static EntityManagerFactory factory = Persistence
			.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	protected EntityManager entityManager;
}
