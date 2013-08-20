package articles.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator {
	/**
	 * Validates the specified date according to the ISO format (yyyy/mm/dd)
	 * and parses it to a Date object. Returns null if the given date
	 * string is not valid.
	 * @param dateToValidate - date string
	 * @return Date object or null if the specified string is not a valid date
	 */
	public Date validateAndParseDate(String dateToValidate) {
		if(dateToValidate == null)
			throw new IllegalArgumentException("Date is empty!");
		try {
			SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy/MM/dd");
			isoFormat.setLenient(false);
			Date date = isoFormat.parse(dateToValidate);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}

}
