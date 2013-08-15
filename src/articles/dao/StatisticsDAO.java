package articles.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import articles.dao.exceptions.StatisticsDAOException;
import articles.model.statistics.Event;
import articles.model.statistics.UserStatistics;
import articles.statistics.dto.UserStatisticsDTO;

/**
 * Gives access to the representation of the UserStatistics class in the
 * database, using defined operations on it.
 * 
 * @author Hristo
 * 
 */
public class StatisticsDAO {
	private static final String PERSISTENCE_UNIT_NAME = "UserPE";
	private static EntityManagerFactory factory;
	private EntityManager entityManager;

	public StatisticsDAO() {
		StatisticsDAO.factory = Persistence
				.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		this.entityManager = StatisticsDAO.factory.createEntityManager();
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
	public void save(int userId, Event event)
			throws StatisticsDAOException {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			entityTransaction.begin();
			UserStatistics statistics = new UserStatistics();
			statistics.setDate(new Date());
			statistics.setEvent(event.getValue());
			statistics.setUser(userId);
			entityManager.persist(statistics);
			entityTransaction.commit();
		} catch (PersistenceException e) {
			throw new StatisticsDAOException(e.getMessage());
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
	public List<UserStatisticsDTO> load(int userId, String date)
			throws StatisticsDAOException {
		try {

			SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd");
			Query selectQuery = this.entityManager
					.createQuery("SELECT us.event, us.eventDate FROM UserStatistics us WHERE SUBSTRING(us.eventDate, 1, 10) = :date AND us.user.userId = :userId");
			selectQuery.setParameter("date", databaseFormat.format(validateDate(date)));
			selectQuery.setParameter("userId", userId);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = selectQuery.getResultList();
			List<UserStatisticsDTO> statisticsList = new ArrayList<UserStatisticsDTO>();
			for (Object[] result : resultList) {
				statisticsList.add(new UserStatisticsDTO((Date) result[1],
						Event.getEvent((int) result[0])));
			}

			return statisticsList;
		} catch (NoResultException | IllegalArgumentException | ParseException e) {
			throw new StatisticsDAOException(e.getMessage());
		}
	}

	private Date validateDate(String dateToValidate) throws ParseException {
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy/MM/dd");
		isoFormat.setLenient(false);
		Date date = isoFormat.parse(dateToValidate);
		return date;
	}
}
