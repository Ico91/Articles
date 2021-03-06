package articles.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import articles.model.UserActivity;

/**
 * Transport object for the UserStatistics class, which contains only the event
 * and its date.
 * 
 * @author Hristo
 * 
 */
@XmlRootElement
public class UserStatisticsDTO {
	private Date activityDate;
	private UserActivity userActivity;
	private String username;
	
	public UserStatisticsDTO() {
	}

	public UserStatisticsDTO(Date activityDate, UserActivity userActivity, String username) {
		this.activityDate = new Date(activityDate.getTime());
		this.userActivity = userActivity;
		this.username = username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userActivity.hashCode();
		result = prime * result + activityDate.hashCode();
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
		if (!activityDate.equals(other.activityDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserStatisticsDTO [activityDate=" + activityDate
				+ ", userActivity=" + userActivity + ", username=" + username +"]";
	}

	public Date getactivityDate() {
		return new Date(activityDate.getTime());
	}

	public void setactivityDate(Date activityDate) {
		this.activityDate = new Date(activityDate.getTime());
	}

	public UserActivity getuserActivity() {
		return userActivity;
	}

	public void setuserActivity(UserActivity userActivity) {
		this.userActivity = userActivity;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
}
