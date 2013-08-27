package articles.model.statistics;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Holds statistics for the specified user, based on his
 * activities. The statistics contains the event itself,
 * the time it happened and the user's id.
 * 
 * @author Hristo
 *
 */
@Entity
@Table(name="statistics")
public class UserStatistics {
	@Id
	@Column(name="event_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int eventId;
	private int userId;
	@Column(name="event_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date activityDate;
	@Column(name="user_activity")
	private UserActivity userActivity;
	
	@Override
	public int hashCode() {
		return eventId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserStatistics))
			return false;
		UserStatistics other = (UserStatistics) obj;
		if (eventId != other.eventId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Statistics [eventId=" + eventId + ", activityDate=" + activityDate + ", event="
				+ userActivity + "]";
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public UserActivity getEvent() {
		return userActivity;
	}

	public void setUserActivity(UserActivity userActivity) {
		this.userActivity = userActivity;
	}
	
}
