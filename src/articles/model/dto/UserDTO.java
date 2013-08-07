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

}
