package articles.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.statistics.dto.UserStatisticsDTO;

public class StatisticsDAO {

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

		//TODO java 7 syntax for generics ?
		TransactionManager<List<UserStatisticsDTO>> manager = new TransactionManager<>();
		List<UserStatisticsDTO> res = (List<UserStatisticsDTO>) manager
				.execute(new TransactionalTask<List<UserStatisticsDTO>>() {

					@Override
					public List<UserStatisticsDTO> executeTask(
							EntityManager entityManager) {
						StatisticsStorage statisticsStorage = new StatisticsStorage(entityManager);
						return statisticsStorage.load(userId, date);
					}
				});

		return res;
	}
}
