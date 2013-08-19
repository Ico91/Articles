package articles.model.statistics;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import articles.model.User;

/**
 * Holds statistics for the specified user, based on his
 * activities. The statistics contains the event itself,
 * the time it happened and the user.
 * 
 * @author Hristo
 *
 */
@Entity
@Table(name="statistics")
public class UserStatistics {
	@Id
	@Column(name="event_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int eventId;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@Column(name="event_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventDate;
	private int event;
	
	public UserStatistics() {
		
	}
	

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
	public User getUser() { 
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setUser(int userId) {
		this.user = new User();
		this.user.setUserId(userId);
	}

	public Date getDate() {
		return eventDate;
	}

	public void setDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eventDate.hashCode();
		result = prime * result + event;
		result = prime * result + eventId;
		return result;
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
		if (!eventDate.equals(other.eventDate))
			return false;
		if (event != other.event)
			return false;
		if (eventId != other.eventId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Statistics [eventId=" + eventId + ", eventDate=" + eventDate + ", event="
				+ event + "]";
	}
	
	
}
