package articles.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import articles.dao.exceptions.StatisticsDAOException;
import articles.model.statistics.Event;
import articles.statistics.dto.UserStatisticsDTO;

public class StatisticsDAOTest {

	private StatisticsDAO statisticsDAO;
	
	@Before
	public void setUp() {
		statisticsDAO = new StatisticsDAO();
	}
	
	@Test
	public void testLoad() throws StatisticsDAOException {
		List<UserStatisticsDTO> expectedResult = new ArrayList<UserStatisticsDTO>();
		expectedResult.add(new UserStatisticsDTO(new Date(Long.valueOf("1376399080000")), Event.LOGIN));
		expectedResult.add(new UserStatisticsDTO(new Date(Long.valueOf("1376399220000")), Event.LOGIN));
		expectedResult.add(new UserStatisticsDTO(new Date(Long.valueOf("1376427264000")), Event.LOGIN));
	
		List<UserStatisticsDTO> actualResult = statisticsDAO.load(111, "2013/08/13");
		
		assertEquals("Returned result should be equal to actual", expectedResult, actualResult);
	}

}
