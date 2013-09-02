package articles.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import articles.model.UserType;

/**
 * Contains information for an user that will be added or updated in the system
 * 
 * @author Galina Hristova
 * 
 */
@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
//	TODO: Rename
public class UserDetails extends LoginRequest {
	@XmlElement(required = true)
	private UserType userType;

	public UserDetails() {

	}

	public UserDetails(String username, String password, UserType userType) {
		super(username, password);
		this.userType = userType;
	}

	@Override
	public String toString() {
		return " [type=" + userType + "]";
	}
	
	public UserType getType() {
		return userType;
	}

	public void setType(UserType userType) {
		this.userType = userType;
	}

}
