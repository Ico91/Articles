package articles.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.UserType;
import articles.model.statistics.UserActivity;
import articles.model.statistics.UserStatistics;
import articles.statistics.dto.UserStatisticsDTO;

public class StatisticsDAOTest {

	private StatisticsDAO statisticsDAO;

	@Before
	public void setUp() {
		statisticsDAO = new StatisticsDAO();
		statisticsDAO.manager.initTestManager();
	}

	@Test
	public void testLoad() {
		initDB();

		List<UserStatisticsDTO> expectedResult = new ArrayList<UserStatisticsDTO>();
		expectedResult.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376399080000")), UserActivity.LOGIN));
		expectedResult.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376399220000")), UserActivity.MODIFY_ARTICLE));
		expectedResult.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376427264000")), UserActivity.LOGOUT));

		Calendar date = Calendar.getInstance();
		date.clear(); // clear hours and minutes
		date.set(2013, 7, 13);
		List<UserStatisticsDTO> actualResult = statisticsDAO.load(111,
				date.getTime());

		assertEquals("Returned result should be equal to actual",
				expectedResult, actualResult);

		destroyDB();
	}

	private void initDB() {

		// TODO: Consider better approach
		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				User user = new User();
				user.setUsername("admin");
				user.setPassword("123");
				user.setLastLogin(Calendar.getInstance().getTime());
				user.setUserId(111);
				user.setUserType(UserType.ADMIN);
				entityManager.persist(user);

				return true;
			}

		});
		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				UserStatistics statistics = new UserStatistics();
				statistics.setUserId(111);
				statistics.setActivityId(1);
				statistics.setActivityDate(new Date(Long
						.parseLong("1376399080000")));
				statistics.setUserActivity(UserActivity.LOGIN);
				entityManager.persist(statistics);

				return true;
			}

		});
		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				UserStatistics statistics = new UserStatistics();
				statistics.setUserId(111);
				statistics.setActivityId(2);
				statistics.setActivityDate(new Date(Long
						.parseLong("1376399220000")));
				statistics.setUserActivity(UserActivity.MODIFY_ARTICLE);
				entityManager.persist(statistics);

				return true;
			}

		});
		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				UserStatistics statistics = new UserStatistics();
				statistics.setUserId(111);
				statistics.setActivityId(3);
				statistics.setActivityDate(new Date(Long
						.parseLong("1376427264000")));
				statistics.setUserActivity(UserActivity.LOGOUT);
				entityManager.persist(statistics);

				return true;
			}

		});
	}

	private void destroyDB() {
		// TODO: Consider better approach
		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				Query deleteQuery = entityManager
						.createNativeQuery("DROP TABLE users");
				deleteQuery.executeUpdate();
				return true;
			}

		});
		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				Query deleteQuery = entityManager
						.createNativeQuery("drop table statistics");
				deleteQuery.executeUpdate();
				return true;
			}

		});
	}
}
