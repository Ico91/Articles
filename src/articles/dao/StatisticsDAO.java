package articles.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.dto.UserStatisticsDTO;
import articles.model.UserActivity;

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
	 * @param activity
	 * @return List of UserStatistics transport objects
	 */
	public List<UserStatisticsDTO> load(final int userId, final Date date, final UserActivity activity) {
		return manager
				.execute(new TransactionalTask<List<UserStatisticsDTO>>() {

					@Override
					public List<UserStatisticsDTO> executeTask(
							EntityManager entityManager) {
						StatisticsStorage statisticsStorage = new StatisticsStorage(
								entityManager);
						if (date != null && activity == null)
							return statisticsStorage.loadByDate(userId, date);
						else if (activity != null && date == null)
							return statisticsStorage.loadByActivity(userId, activity);
						else if (date != null && activity != null) 
							return statisticsStorage.loadByDateActivity(userId, date, activity);
						else 
							return statisticsStorage.loadAll(userId);
					}
				});
	}
	
	/**
	 * Loads information about user activities for all users, according to the
	 * specified date and activity.
	 * 
	 * @param date
	 * @param activity
	 * @return Map of user id as keys and list of
	 *         {@link articles.model.dto.UserStatisticsDTO} as values
	 */
	public Map<Integer, List<UserStatisticsDTO>> loadAll(final Date date, final UserActivity activity) {
		return manager
				.execute(new TransactionalTask<Map<Integer, List<UserStatisticsDTO>>>() {

					@Override
					public Map<Integer, List<UserStatisticsDTO>> executeTask(
							EntityManager entityManager) {
						StatisticsStorage statisticsStorage = new StatisticsStorage(
								entityManager);
						Map<Integer, List<UserStatisticsDTO>> result = new HashMap<Integer, List<UserStatisticsDTO>>();
						for (int uId : statisticsStorage.getUsers()) {
							if (date != null && activity == null)
								result.put(uId, statisticsStorage.loadByDate(uId, date));
							else if (activity != null && date == null)
								result.put(uId, statisticsStorage.loadByActivity(uId, activity));
							else if (date != null && activity != null) 
								result.put(uId, statisticsStorage.loadByDateActivity(uId, date, activity));
							else 
								result.put(uId, statisticsStorage.loadAll(uId));
						}
						return result;
					}

				});
	}
}
