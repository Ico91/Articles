package articles.model.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import articles.model.User;

@XmlRootElement
public class UserDTO {
	private int userId;
	private String username;
	private Date lastLogin;

	public UserDTO() {

	}

	public UserDTO(User user) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.lastLogin = user.getLastLogin();
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

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lastLogin == null) ? 0 : lastLogin.hashCode());
		result = prime * result + userId;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserDTO))
			return false;
		UserDTO other = (UserDTO) obj;
		if (!lastLogin.equals(other.lastLogin))
			return false;
		if (userId != other.userId)
			return false;
		if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", username=" + username
				+ ", lastLogin=" + lastLogin + "]";
	}

}
