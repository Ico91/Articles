package articles.database.transactions;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import articles.dao.PersistenceDAO;

public class TransactionManager<T> extends PersistenceDAO {
	
	public TransactionManager() {
		super();
	}
	
	public T execute(TransactionalTask<T> task) {
		EntityTransaction entityTransaction = super.entityManager.getTransaction();
		try {
			entityTransaction.begin();
			T result = (T) task.executeTask(entityManager);
			entityTransaction.commit();
			return result;
		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			if(entityTransaction.isActive())
			{
				entityTransaction.rollback();
			}
		}
	}
}
