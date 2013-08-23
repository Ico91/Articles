package articles.web.resources.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class DateAdapter {

	private Date date;

	public DateAdapter(String dateInput) throws WebApplicationException {
		final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy/MM/dd");
		try {
			isoFormat.setLenient(false);
			date = isoFormat.parse(dateInput);
		} catch (ParseException e) {
			throw new WebApplicationException(Response
					.status(Status.BAD_REQUEST)
					.entity("Date string is invalid: " + e.getMessage())
					.build());
		}
	}

	public Date getDate() {
		return date;
	}
}
