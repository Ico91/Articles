package articles.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.model.UserActivity;
import articles.model.UserStatistics;

/**
 * Provides methods for accessing statistics information from database
 * 
 * @author Hristo
 */
public class StatisticsDAO extends DAOBase {

	public StatisticsDAO() {
		super();
	}

	public StatisticsDAO(TransactionManager transactionManager) {
		super(transactionManager);
	}
	
	//	TODO: Comments
	/**
	 * Loads information about user activities for a specified date, given the
	 * user id.
	 * 
	 * @param userId
	 *            - id of the user
	 * @param date
	 *            - date to get the activities from
	 * @param activity
	 * @return List of UserStatistics transport objects
	 */
	public List<UserStatistics> loadUserStatistics(final int userId,
			final Date date, final UserActivity activity, final int from,
			final int to) {
		return manager.execute(new TransactionalTask<List<UserStatistics>>() {

			@Override
			public List<UserStatistics> executeTask(EntityManager entityManager) {
				//	TODO: Similar
				StatisticsStorage statisticsStorage = new StatisticsStorage(
						entityManager);
				return statisticsStorage.getUserStatistics(userId, date,
						activity, from, to);
			}
		});
	}
	
	//	TODO: Comments
	/**
	 * Loads information about user activities for all users, according to the
	 * specified date and activity.
	 * 
	 * @param date
	 * @param activity
	 * @return List of activities
	 */
	public List<UserStatistics> loadStatistics(final Date date,
			final UserActivity activity, final int from, final int to) {
		return manager.execute(new TransactionalTask<List<UserStatistics>>() {

			@Override
			public List<UserStatistics> executeTask(EntityManager entityManager) {
				StatisticsStorage statisticsStorage = new StatisticsStorage(entityManager);
				return statisticsStorage.getStatistics(date, activity, from, to);
			}
		});
	}
}
