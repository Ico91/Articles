package articles.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.model.UserActivity;
import articles.model.UserStatistics;

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
		List<UserStatistics> statistics = statisticsDAO.loadStatistics(null, null, 0, 0);
		assertTrue(statistics.size() == 3);
	}

	@Test
	public void testLoadSingleUser() {
		assertTrue(statisticsDAO.loadUserStatistics(1, null, null, 0, 0).size() == 1);
	}

	@AfterClass
	public static void cleanUp() {
		destroyDB();
	}

	private static void initDB() {
		statisticsDAO.manager.execute(new TransactionalTask<UserStatistics>() {

			@Override
			public UserStatistics executeTask(EntityManager entityManager) {
				for(int i = 0; i < 3; i ++) {
					statisticsDAO.addToStatistics(i, entityManager, UserActivity.values()[i]);
				}
				return null;
			}
		});
	}

	private static void destroyDB() {
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
