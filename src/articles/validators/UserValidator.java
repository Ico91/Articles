package articles.validators;

import java.util.ArrayList;
import java.util.List;

import articles.dto.UserDetails;
import articles.messages.MessageKey;
import articles.messages.UserMessageKeys;
import articles.model.User;

/**
 * Validate users
 * 
 * @author Galina Hristova
 *
 */
public class UserValidator implements Validator {
	private UserDetails userToCheck;
	private List<User> users;
	
	public UserValidator(UserDetails userToCheck, List<User> users) {
		this.userToCheck = userToCheck;
		this.users = users;
	}
	
	/**
	 * Validate username, password and user type
	 * 
	 *  @return List of MessageKeys
	 */
	@Override
	public List<MessageKey> validate() {
		List<MessageKey> listOfMessageKeys = new ArrayList<MessageKey>();
		
		if (userToCheck.getUsername() == null)
			listOfMessageKeys.add(UserMessageKeys.USERNAME_IS_NULL);
		else if (userToCheck.getUsername().isEmpty())
			listOfMessageKeys.add(UserMessageKeys.USERNAME_IS_EMPTY);
		
		if (userToCheck.getPassword() == null)
			listOfMessageKeys.add(UserMessageKeys.PASSWORD_IS_NULL);
		else if (userToCheck.getPassword().isEmpty())
			listOfMessageKeys.add(UserMessageKeys.PASSWORD_IS_EMPTY);
		
		if (userToCheck.getType() == null)
			listOfMessageKeys.add(UserMessageKeys.USERTYPE_IS_NULL);
		
		if (!isUsernameUnique())
			listOfMessageKeys.add(UserMessageKeys.NOT_UNIQUE_USERNAME);
		
		return listOfMessageKeys;
	}
	
	/**
	 * Check if username is unique
	 * 
	 * @return true if the username is unique, otherwise false
	 */
	private boolean isUsernameUnique() {
		for (User u : users) {
			if (u.getUsername().equals(userToCheck.getUsername())) {
				return false;
			}
		}
		
		return true;
	}
	
}
