package articles.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.dto.UserStatisticsDTO;
import articles.model.User;
import articles.model.UserActivity;
import articles.model.UserStatistics;
import articles.model.UserType;

public class StatisticsDAOTest {
	private static TransactionManager manager = new TransactionManager();
	private static StatisticsDAO statisticsDAO;
	
	@BeforeClass
	public static void init() {
		manager.initTestManager();
		statisticsDAO = new StatisticsDAO(manager);
		initDB();
	}

	@Test
	public void testLoadAllUsers() {		
		List<UserStatisticsDTO> expectedUser = new ArrayList<UserStatisticsDTO>();
		expectedUser.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376399080000")), UserActivity.CREATE_ARTICLE));
		expectedUser.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376399220000")), UserActivity.MODIFY_ARTICLE));
		expectedUser.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376427264000")), UserActivity.DELETE_ARTICLE));
		List<UserStatisticsDTO> expectedAdmin = new ArrayList<UserStatisticsDTO>();
		expectedAdmin.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376399080000")), UserActivity.LOGIN));
		expectedAdmin.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376399220000")), UserActivity.MODIFY_ARTICLE));
		expectedAdmin.add(new UserStatisticsDTO(new Date(Long
				.valueOf("1376427264000")), UserActivity.LOGOUT));
		Map<Integer, List<UserStatisticsDTO>> expectedResult = new HashMap<Integer, List<UserStatisticsDTO>>();
		expectedResult.put(111, expectedAdmin);
		expectedResult.put(112, expectedUser);

		Calendar date = Calendar.getInstance();
		date.clear(); // clear hours and minutes
		date.set(2013, 7, 13);
		/*Map<Integer, List<UserStatisticsDTO>> actualResult = statisticsDAO
				.load(date.getTime());*/
		
	/*	assertEquals("Returned result should be equal to actual",
				expectedResult, actualResult);*/
			
	}

	@Test
	public void testLoadSingleUser() {
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
		/*List<UserStatisticsDTO> actualResult = statisticsDAO.load(111,
				date.getTime());

		assertEquals("Returned result should be equal to actual",
				expectedResult, actualResult);*/
	}

	@AfterClass
	public static void cleanUp() {
		destroyDB();
	}

	private static void initDB() {

		// TODO: Consider better approach
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

		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				UserStatistics statistics = new UserStatistics();
				statistics.setUserId(112);
				statistics.setActivityId(4);
				statistics.setActivityDate(new Date(Long
						.parseLong("1376399080000")));
				statistics.setUserActivity(UserActivity.CREATE_ARTICLE);
				entityManager.persist(statistics);

				return true;
			}

		});
		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				UserStatistics statistics = new UserStatistics();
				statistics.setUserId(112);
				statistics.setActivityId(5);
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
				statistics.setUserId(112);
				statistics.setActivityId(6);
				statistics.setActivityDate(new Date(Long
						.parseLong("1376427264000")));
				statistics.setUserActivity(UserActivity.DELETE_ARTICLE);
				entityManager.persist(statistics);

				return true;
			}

		});

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
				User user = new User();
				user.setUsername("user");
				user.setPassword("321");
				user.setLastLogin(Calendar.getInstance().getTime());
				user.setUserId(112);
				user.setUserType(UserType.USER);
				entityManager.persist(user);

				return true;
			}

		});
	}

	private static void destroyDB() {
		// TODO: Consider better approach
		statisticsDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				Query deleteQuery = entityManager
						.createNativeQuery("drop table users");
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
