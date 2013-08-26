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
	private UserActivity userActivity;
	
	public UserStatisticsDTO() { }

	public UserStatisticsDTO(Date eventDate, UserActivity userActivity) {
		this.eventDate = eventDate;
		this.userActivity = userActivity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userActivity.hashCode();
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
		if (userActivity != other.userActivity)
			return false;
		if (!eventDate.equals(other.eventDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserStatisticsDTO [eventDate=" + eventDate + ", userActivity=" + userActivity
				+ "]";
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public UserActivity getuserActivity() {
		return userActivity;
	}

	public void setuserActivity(UserActivity userActivity) {
		this.userActivity = userActivity;
	}
}
