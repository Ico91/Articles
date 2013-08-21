package articles.database.transactions;

import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import articles.dao.PersistenceDAO;
import articles.dao.exceptions.PersistenceDAOException;

public class TransactionManager<T> extends PersistenceDAO {
	static final Logger logger = Logger.getLogger(TransactionManager.class);
	
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
		} catch (PersistenceDAOException e) {
			logger.error(e.getMessage());
			throw new PersistenceDAOException(e.getMessage());
		} finally {
			if(entityTransaction.isActive())
			{
				entityTransaction.rollback();
			}
		}
	}
}
