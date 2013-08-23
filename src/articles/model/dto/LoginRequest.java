package articles.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**Contains the entered username and password.
 * @author Galina Hristova
 *
 */
//TODO why this class is in this package
//TODO why this package exists at all

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginRequest{
	@XmlElement(required = true)
	private String username;
	@XmlElement(required = true)
	private String password;
	
	public LoginRequest() {
		
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
		LoginRequest other = (LoginRequest) obj;
		if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoginRequest [username=" + username + ", password=" + password
				+ "]";
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

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
