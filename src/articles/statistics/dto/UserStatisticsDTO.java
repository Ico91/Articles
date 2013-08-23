package articles.statistics.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import articles.model.statistics.UserActivity;

/**
 * Transport object for the UserStatistics class, which contains only the event
 * and its date.
 * 
 * @author Hristo
 * 
 */
@XmlRootElement
public class UserStatisticsDTO {
	private Date eventDate;
	private UserActivity event;
	
	public UserStatisticsDTO() { }

	public UserStatisticsDTO(Date eventDate, UserActivity event) {
		this.eventDate = eventDate;
		this.event = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + event.hashCode();
		result = prime * result + eventDate.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserStatisticsDTO))
			return false;
		UserStatisticsDTO other = (UserStatisticsDTO) obj;
		if (event != other.event)
			return false;
		if (!eventDate.equals(other.eventDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserStatisticsDTO [eventDate=" + eventDate + ", event=" + event
				+ "]";
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public UserActivity getEvent() {
		return event;
	}

	public void setEvent(UserActivity event) {
		this.event = event;
	}
}
