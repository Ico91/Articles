package articles.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import articles.dao.exceptions.DAOException;
import articles.database.StatisticsQueryBuilder;
import articles.model.UserActivity;
import articles.model.UserStatistics;

/**
 * Provides methods saving and loading statistics from the database
 * 
 * @author Hristo
 * 
 */
class StatisticsStorage {
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
	
	//	TODO: Coments
	public List<UserStatistics> getUserStatistics(int userId, Date date,
			UserActivity activity, int from, int to) {

		StatisticsQueryBuilder qBuilder = new StatisticsQueryBuilder(entityManager);
		qBuilder.filterByUserId(userId);
		return executeQuery(date, activity, from, to, qBuilder);
	}

	// TODO:Coments
	public List<UserStatistics> getStatistics(Date date, UserActivity activity,
			int from, int to) {
		StatisticsQueryBuilder qBuilder = new StatisticsQueryBuilder(
				entityManager);
		return executeQuery(date, activity, from, to, qBuilder);
	}

	// TODO: Coments
	@SuppressWarnings("unchecked")
	private List<UserStatistics> executeQuery(Date date, UserActivity activity,
			int from, int to, StatisticsQueryBuilder qBuilder) {
		if (date != null)
			qBuilder.filterByDate(date);
		if (activity != null)
			qBuilder.filterByActivity(activity);
		Query q = qBuilder.build();
		q.setFirstResult(from);
		
		if (to - from >= 0)
			q.setMaxResults(to - from);

		return q.getResultList();
	}

	/**
	 * Loads all users' id from the statistics table.
	 * 
	 * @return Collection holding unique user identifications.
	 */
	@SuppressWarnings("unchecked")
	public Set<Integer> getUsers() {
		Query selectQuery = entityManager.createNativeQuery(LOAD_USER_IDS);
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
