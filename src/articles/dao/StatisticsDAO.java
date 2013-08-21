package articles.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.statistics.dto.UserStatisticsDTO;

public class StatisticsDAO {

	public List<UserStatisticsDTO> load(final int userId, final Date date) {

		TransactionManager<List<UserStatisticsDTO>> manager = new TransactionManager<List<UserStatisticsDTO>>();
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
