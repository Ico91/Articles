package articles.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.dao.exceptions.StatisticsDAOException;
import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.model.statistics.UserActivity;
import articles.model.statistics.UserStatistics;
import articles.statistics.dto.UserStatisticsDTO;

/**
 * Provides methods saving and loading statistics from the database
 * 
 * @author Hristo
 * 
 */
public class StatisticsDAO {
	static final Logger logger = Logger.getLogger(StatisticsDAO.class);

	public StatisticsDAO() {
		super();
	}

	/**
	 * Saves a new record for a user activity in the database.
	 * 
	 * @param userId
	 *            - the id of the user
	 * @param event
	 *            - specified by the Event enumeration
	 * @throws StatisticsDAOException
	 */	
	public boolean save(final int userId, final UserActivity event) {
		TransactionManager<Boolean> manager = new TransactionManager<Boolean>();
		boolean res = false;
		if (manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) throws PersistenceException {
				UserStatistics statistics = new UserStatistics();
				statistics.setDate(new Date());
				statistics.setEvent(event.getValue());
				statistics.setUser(userId);
				entityManager.persist(statistics);
				entityManager.flush();
				
				return Boolean.TRUE;
			}

		}))
			res = true;
		return res;
	}

	/**
	 * Loads information about user activities for a specified date, given the
	 * user id.
	 * 
	 * @param userId
	 *            - id of the user
	 * @param date
	 *            - of type String which will be validated under the ISO format
	 *            (YYYY/MM/DD)
	 * @return List of UserStatistics transport objects
	 * @throws StatisticsDAOException
	 */
	
	@SuppressWarnings("unchecked")
	public List<UserStatisticsDTO> load(final int userId, final Date date) {

		TransactionManager<List<UserStatisticsDTO>> manager = new TransactionManager<List<UserStatisticsDTO>>();
		List<UserStatisticsDTO> res = (List<UserStatisticsDTO>) manager
				.execute(new TransactionalTask<List<UserStatisticsDTO>>() {

					@Override
					public List<UserStatisticsDTO> executeTask(EntityManager entityManager) {
						SimpleDateFormat databaseFormat = new SimpleDateFormat(
								"yyyy-MM-dd");
						Query selectQuery = entityManager
								.createNativeQuery("SELECT event, event_date FROM statistics WHERE DATE(event_date) = ?1 AND user_id = ?2");
						selectQuery.setParameter(1, databaseFormat.format(date));
						selectQuery.setParameter(2, userId);

						List<Object[]> resultList = selectQuery.getResultList();
						List<UserStatisticsDTO> statisticsList = new ArrayList<UserStatisticsDTO>();
						for (Object[] result : resultList) {
							statisticsList.add(new UserStatisticsDTO(
									(Date) result[1],
									UserActivity.values()[(int) result[0] - 1]));
						}
						return statisticsList;
					}
				});

		return res;

	}
}
