package articles.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.dto.UserStatisticsDTO;

/**
 * Provides methods for accessing statistics information from database
 * 
 * @author Hristo
 */
public class StatisticsDAO extends DAOBase {
	private UserDAO userDAO;

	public StatisticsDAO() {
		userDAO = new UserDAO();
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
	 * @return Map of user id as keys and list of {@link articles.model.dto.UserStatisticsDTO} as values
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
						for (User u : userDAO.getUsers()) {
							result.put(u.getUserId(),
									statisticsStorage.load(u.getUserId(), date));
						}
						return result;
					}

				});
	}
}
