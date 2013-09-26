package articles.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import articles.dao.exceptions.DAOException;
import articles.database.transactions.TransactionManager;
import articles.model.UserActivity;
import articles.validators.MessageKey;
import articles.validators.Validator;

/**
 * Base class for all DAO objects
 * 
 * @author Krasimir Atanasov
 * 
 */
public class DAOBase {
	protected TransactionManager manager;
	protected Logger logger;

	public DAOBase() {
		this.manager = new TransactionManager();
		this.logger = Logger.getLogger(getClass());
	}
	
	public DAOBase(TransactionManager transactionManager) {
		this.manager = transactionManager;
		this.logger = Logger.getLogger(getClass());
	}

	/**
	 * Save user activity in statistics
	 * 
	 * @param userId
	 *            ID of the user
	 * @param entityManager
	 * @param activity
	 *            UserActivity
	 */
	public void addToStatistics(final int userId, EntityManager entityManager,
			UserActivity activity) {
		StatisticsStorage storage = new StatisticsStorage(entityManager);
		storage.save(userId, activity);
	}

	/**
	 * Defines if any errors have occurred
	 * 
	 * @param validator
	 */
	public void validate(Validator validator) {
		List<MessageKey> messageKeys = validator.validate();

		if (messageKeys.size() != 0) {
			logger.error("Validation errors occured!");

			throw new DAOException("Validation errors occured!");
		}
	}
}
