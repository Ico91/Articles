package articles.database.transactions;

import javax.persistence.EntityManager;

public interface TransactionalTask<T> {
	T executeTask(EntityManager entityManager);
}
