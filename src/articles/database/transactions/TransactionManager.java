package articles.database.transactions;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import articles.dao.exceptions.DAOException;

/**
 * Handles transactions for database requests.
 * 
 * @author Hristo
 * 
 * @param <T>
 */
public class TransactionManager extends PersistenceManager {
	private static final String TRANSACTION_ERROR = "Problem occurs in the transaction.";
	private Logger logger = Logger.getLogger(getClass());

	public TransactionManager() {
		super();
	}

	/**
	 * Executes the provided instance of the TransactionalTask interface in a
	 * single database transaction. Throws RuntimeException in case of commit
	 * failure.
	 * 
	 * @param task
	 *            - instance of the TransactionalTask interface.
	 * @return
	 */
	public <T> T execute(TransactionalTask<T> task) throws RuntimeException {
		EntityTransaction entityTransaction = super.entityManager
				.getTransaction();
		try {
			entityTransaction.begin();
			T result = (T) task.executeTask(entityManager);
			entityTransaction.commit();
			return result;
		} catch (PersistenceException e) {
			logger.error(e.getMessage());
			throw new DAOException(TRANSACTION_ERROR);
		} finally {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
		}
	}
}
