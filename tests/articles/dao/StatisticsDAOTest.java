package articles.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import articles.model.statistics.UserActivity;
import articles.statistics.dto.UserStatisticsDTO;

public class StatisticsDAOTest {

	private StatisticsDAO statisticsDAO;
	
	@Before
	public void setUp() {
		statisticsDAO = new StatisticsDAO();
	}
	
	@Test
	public void testLoad() {
		List<UserStatisticsDTO> expectedResult = new ArrayList<UserStatisticsDTO>();
		expectedResult.add(new UserStatisticsDTO(new Date(Long.valueOf("1376399080000")), UserActivity.LOGIN));
		expectedResult.add(new UserStatisticsDTO(new Date(Long.valueOf("1376399220000")), UserActivity.LOGIN));
		expectedResult.add(new UserStatisticsDTO(new Date(Long.valueOf("1376427264000")), UserActivity.LOGIN));
	
		Calendar date = Calendar.getInstance();
		date.clear(); // clear hours and minutes
		date.set(2013, 8, 13);
		List<UserStatisticsDTO> actualResult = statisticsDAO.load(111, date.getTime());
		
		assertEquals("Returned result should be equal to actual", expectedResult, actualResult);
	}

}
