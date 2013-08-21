package articles.database.transactions;

import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;

import articles.dao.PersistenceDAO;
import articles.dao.exceptions.PersistenceDAOException;

/**
 * Handles transactions for database requests.
 * @author Hristo
 *
 * @param <T>
 */
public class TransactionManager<T> extends PersistenceDAO {
	static final Logger logger = Logger.getLogger(TransactionManager.class);
	
	public TransactionManager() {
		super();
	}
	
	/**
	 * Executes the provided instance of the TransactionalTask interface in a single
	 * database transaction. Throws PersistenceDAOException in case of commit failure.
	 * @param task - instance of the TransactionalTask interface.
	 * @return
	 */
	public T execute(TransactionalTask<T> task) throws TransactionException {
		EntityTransaction entityTransaction = super.entityManager.getTransaction();
		try {
			entityTransaction.begin();
			T result = (T) task.executeTask(entityManager);
			entityTransaction.commit();
			return result;
		} catch (PersistenceDAOException | RollbackException e) {
			logger.error(e.getMessage());
			throw new TransactionException(e.getMessage());
		} finally {
			if(entityTransaction.isActive())
			{
				entityTransaction.rollback();
			}
		}
	}
}
