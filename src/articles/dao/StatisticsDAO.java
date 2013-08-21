package articles.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

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
public class StatisticsDAO extends PersistenceDAO {
	static final Logger logger = Logger.getLogger(StatisticsDAO.class);

	public StatisticsDAO() {
		this.entityManager = PersistenceDAO.factory.createEntityManager();
	}

	/**
	 * Saves a new record of user activity in the database.
	 * 
	 * @param userId
	 *            - the id of the user
	 * @param event
	 *            - specified by the Event enumeration
	 * @throws StatisticsDAOException
	 */
	public void save(int userId, UserActivity event) throws StatisticsDAOException {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			entityTransaction.begin();
			UserStatistics statistics = new UserStatistics();
			statistics.setDate(new Date());
			statistics.setEvent(event.getValue());
			statistics.setUser(userId);
			entityManager.persist(statistics);
		} catch (PersistenceException e) {
			entityTransaction.rollback();
			logger.error("Error, while saving activity statistics for user with user id = " + userId);
			throw new StatisticsDAOException(e.getMessage());
		} finally {
			if(entityTransaction.isActive())
				entityTransaction.commit();
		}
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
	public List<UserStatisticsDTO> load(int userId, Date date) {

		SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd");
		Query selectQuery = this.entityManager
				.createNativeQuery("SELECT event, event_date FROM statistics WHERE DATE(event_date) = ?1 AND user_id = ?2");
		selectQuery.setParameter(1, databaseFormat.format(date));
		selectQuery.setParameter(2, userId);

		return getStatisticsFromDatabase(selectQuery);
	}

	@SuppressWarnings("unchecked")
	private List<UserStatisticsDTO> getStatisticsFromDatabase(Query selectQuery) {
		List<Object[]> resultList = selectQuery.getResultList();
		List<UserStatisticsDTO> statisticsList = new ArrayList<UserStatisticsDTO>();
		for (Object[] result : resultList) {
			statisticsList.add(new UserStatisticsDTO((Date) result[1], UserActivity.values()[((int) result[0])]));
		}
		return statisticsList;
	}

}
