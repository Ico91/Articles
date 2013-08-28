package articles.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.dao.exceptions.DAOException;
import articles.model.UserActivity;
import articles.model.UserStatistics;
import articles.model.dto.UserStatisticsDTO;

/**
 * Provides methods saving and loading statistics from the database
 * 
 * @author Hristo
 * 
 */
class StatisticsStorage {
	private static final String LOAD_USER_STATISTICS = "SELECT user_activity, activity_date "
			+ "FROM statistics WHERE DATE(activity_date) = ?1 AND userid = ?2";
	private static final String LOAD_USER_IDS = "SELECT userid FROM statistics";

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
	 */
	public void save(final int userId, final UserActivity event) {
		UserStatistics statistics = new UserStatistics();
		statistics.setActivityDate(new Date());
		statistics.setUserActivity(event);
		statistics.setUserId(userId);
		try {
			entityManager.persist(statistics);
		} catch (PersistenceException e) {
			logger.error("Error while saving activity statistics "
					+ event.toString() + ", for user with user id: " + userId);
			throw new DAOException("Error while saving activity statistics "
					+ event.toString() + ", for user with user id: " + userId);
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
				.createNativeQuery(LOAD_USER_STATISTICS);
		selectQuery.setParameter(1, databaseFormat.format(date));
		selectQuery.setParameter(2, userId);
		try {
			List<Object[]> resultList = selectQuery.getResultList();
			List<UserStatisticsDTO> statisticsList = new ArrayList<UserStatisticsDTO>();
			for (Object[] result : resultList) {
				statisticsList.add(new UserStatisticsDTO((Date) result[1],
						UserActivity.values()[(int) result[0]]));
			}
			return statisticsList;
		} catch (PersistenceException e) {
			throw new DAOException(
					"Error while loading statistics for user with user id: "
							+ userId);
		}

	}

	@SuppressWarnings("unchecked")
	public Set<Integer> getUsers() {
		Query selectQuery = entityManager
				.createNativeQuery(LOAD_USER_IDS);
		try {
			Set<Integer> resultList = new HashSet<Integer>();
			resultList.addAll(selectQuery.getResultList());
			return resultList;
		} catch (PersistenceException e) {
			throw new DAOException(
					"Error while loading user ids from statistics table");
		}
	}
}
