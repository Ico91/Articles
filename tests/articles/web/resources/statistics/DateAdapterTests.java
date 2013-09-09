package articles.web.resources.statistics;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import articles.web.resources.DateAdapter;

@RunWith(value = Parameterized.class)
public class DateAdapterTests {

	private String date;
	DateAdapter actualDate;

	public DateAdapterTests(String date) {
		this.date = date;
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { {"2013/08/32"}, {"2013/02/29"}, {""}, {"20/08/2013"}, {"2013-08-32"} };
		return Arrays.asList(data);
	}
	
	@Test
	public void testDateAdapter() {
		actualDate = new DateAdapter("2013/08/20");
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.clear();
		expectedDate.set(2013, 7, 20);
		assertEquals("Expected date should be equal to actual",
				expectedDate.getTime(), actualDate.getDate());
	}
	
	@Test
	public void testLeapYear() {
		actualDate = new DateAdapter("2012/02/29");
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.clear();
		expectedDate.set(2012, 1, 29);
		assertEquals("Expected date should be equal to actual",
				expectedDate.getTime(), actualDate.getDate());
	}
	
	@Test (expected = WebApplicationException.class)
	public void testWrongDates() {
		actualDate = new DateAdapter(date);
	}
}
