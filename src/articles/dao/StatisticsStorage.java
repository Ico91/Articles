package articles.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.dao.exceptions.PersistenceDAOException;
import articles.dao.exceptions.StatisticsDAOException;
import articles.model.statistics.UserActivity;
import articles.model.statistics.UserStatistics;
import articles.statistics.dto.UserStatisticsDTO;

/**
 * Provides methods saving and loading statistics from the database
 * 
 * @author Hristo
 * 
 */
class StatisticsStorage {
	private EntityManager entityManager;
	private Logger logger = Logger.getLogger(getClass());

	public StatisticsStorage(EntityManager entityManager) {
		this.entityManager = entityManager;
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
	public void save(final int userId, final UserActivity event)
			throws StatisticsStorageException {
		UserStatistics statistics = new UserStatistics();
		statistics.setDate(new Date());
		statistics.setUserActivity(event);
		statistics.setUserId(userId);
		try {
			entityManager.persist(statistics);
		} catch (PersistenceException e) {
			logger.error("Error while saving activity statistics "
					+ event.toString() + ", for user with user id: " + userId);
			throw new PersistenceDAOException(
					"Error while saving activity statistics "
							+ event.toString() + ", for user with user id: "
							+ userId);
		}
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
	 * @throws StatisticsDAOException
	 */

	@SuppressWarnings("unchecked")
	public List<UserStatisticsDTO> load(final int userId, final Date date) {

		SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd");
		Query selectQuery = entityManager
				.createNativeQuery("SELECT event, event_date FROM statistics WHERE DATE(event_date) = ?1 AND userid = ?2");
		selectQuery.setParameter(1, databaseFormat.format(date));
		selectQuery.setParameter(2, userId);
		try {
			List<Object[]> resultList = selectQuery.getResultList();
			List<UserStatisticsDTO> statisticsList = new ArrayList<UserStatisticsDTO>();
			for (Object[] result : resultList) {
				statisticsList.add(new UserStatisticsDTO((Date) result[1],
						UserActivity.values()[(int) result[0] - 1]));
			}
			return statisticsList;
		} catch (PersistenceException e) {
			throw new StatisticsStorageException(
					"Error while loading statistics for user with user id: "
							+ userId);
		}

	}
}
