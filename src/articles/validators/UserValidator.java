package articles.validators;

import java.util.ArrayList;
import java.util.List;

import articles.model.User;
import articles.model.dto.NewUserRequest;

public class UserValidator implements Validator {
	private NewUserRequest userToCheck;
	private List<User> users;
	
	public UserValidator(NewUserRequest userToCheck, List<User> users) {
		this.userToCheck = userToCheck;
		this.users = users;
	}
	
	@Override
	public List<MessageKey> validate() {
		List<MessageKey> listOfMessageKeys = new ArrayList<MessageKey>();
		
		if (userToCheck.getUsername() == null)
			listOfMessageKeys.add(UserErrorMessageKeys.USERNAME_IS_NULL);
		else if (userToCheck.getUsername().isEmpty())
			listOfMessageKeys.add(UserErrorMessageKeys.USERNAME_IS_EMPTY);
		
		if (userToCheck.getPassword() == null)
			listOfMessageKeys.add(UserErrorMessageKeys.PASSWORD_IS_NULL);
		else if (userToCheck.getPassword().isEmpty())
			listOfMessageKeys.add(UserErrorMessageKeys.PASSWORD_IS_EMPTY);
		
		if (userToCheck.getType() == null)
			listOfMessageKeys.add(UserErrorMessageKeys.USERTYPE_IS_NULL);
		
		if (!isUsernameUnique())
			listOfMessageKeys.add(UserErrorMessageKeys.NOT_UNIQUE_USERNAME);
		
		return listOfMessageKeys;
	}
	
	private boolean isUsernameUnique() {
		for (User u : users) {
			if (u.getUsername().equals(userToCheck.getUsername())) {
				return false;
			}
		}
		
		return true;
	}
	
}
