package articles.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.dto.UserStatisticsDTO;

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
	
	/**
	 * Loads information about user activities for a specified date, given the
	 * user id.
	 * 
	 * @param userId
	 *            - id of the user
	 * @param date
	 *            - date to get the activities from
	 * @return List of UserStatistics transport objects
	 */
	public List<UserStatisticsDTO> load(final int userId, final Date date) {
		return manager
				.execute(new TransactionalTask<List<UserStatisticsDTO>>() {

					@Override
					public List<UserStatisticsDTO> executeTask(
							EntityManager entityManager) {
						StatisticsStorage statisticsStorage = new StatisticsStorage(
								entityManager);
						return statisticsStorage.load(userId, date);
					}
				});
	}

	/**
	 * Loads information about user activities for all users, according to the
	 * specified date.
	 * 
	 * @param date
	 * @return Map of user id as keys and list of
	 *         {@link articles.model.dto.UserStatisticsDTO} as values
	 */
	public Map<Integer, List<UserStatisticsDTO>> load(final Date date) {
		return manager
				.execute(new TransactionalTask<Map<Integer, List<UserStatisticsDTO>>>() {

					@Override
					public Map<Integer, List<UserStatisticsDTO>> executeTask(
							EntityManager entityManager) {
						StatisticsStorage statisticsStorage = new StatisticsStorage(
								entityManager);
						Map<Integer, List<UserStatisticsDTO>> result = new HashMap<Integer, List<UserStatisticsDTO>>();
						for (int uId : statisticsStorage.getUsers()) {
							result.put(uId, statisticsStorage.load(uId, date));
						}
						return result;
					}

				});
	}
}
