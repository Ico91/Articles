package articles.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import articles.dao.exceptions.DAOException;
import articles.database.transactions.TransactionManager;
import articles.model.statistics.UserActivity;
import articles.validators.ErrorMessageBuilder;
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

	// TODO: logger initialization ?
	public DAOBase() {
		this.manager = new TransactionManager();
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

	public void validate(Validator validator) {
		List<MessageKey> messageKeys = validator.validate();

		if (messageKeys.size() != 0) {
			ErrorMessageBuilder builder = new ErrorMessageBuilder(messageKeys);
			logger.error(builder.getMessage().getMessage());

			throw new DAOException(builder.getMessage().getMessage());
		}
	}
}
