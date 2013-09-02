package articles.web.resources.statistics;

import static org.junit.Assert.*;

import java.util.Calendar;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;

import articles.web.resources.DateAdapter;

public class DateAdapterTests {

	DateAdapter actualDate;

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
	public void testInvalidDate() {
		actualDate = new DateAdapter("2013/08/32");
	}
	
	@Test (expected = WebApplicationException.class)
	public void testNotLeapYear() {
		actualDate = new DateAdapter("2013/02/29");
	}
	
	@Test (expected = WebApplicationException.class)
	public void testEmptyDate() {
		actualDate = new DateAdapter("");
	}

	@Test (expected = WebApplicationException.class)
	public void testWrongDate() {
		actualDate = new DateAdapter("20/08/2013");
	}
	
	@Test (expected = WebApplicationException.class)
	public void testWrongDateFormat() {
		actualDate = new DateAdapter("2013-08-32");
	}
}
