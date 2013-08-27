package articles.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/** Stores data for users in the system.
 * Each user has an unique username.
 * @author Galina Hristova
 *
 */
@Entity
@Table(name="user")
@XmlRootElement
public class User {
	@Id
	@Column(name="userId")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int userId;
	private String username;
	private String password;
	@Column(name="last_login")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
	public User() {
	
	}
	
	public User(int userId, String username, String password, UserType userType ) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username
				+ ", password=" + password + ", lastLogin=" + lastLogin
				+ ", type=" + userType + "]";
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (!username.equals(other.username))
			return false;
		return true;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
}
