package articles.validators;

/**
 * Defines the error messages used while validating an user
 * 
 * @author Galina Hristova
 *
 */
public enum UserErrorMessageKeys implements MessageKey {
	USERNAME_IS_NULL("Username is null"),
	USERNAME_IS_EMPTY("Username is empty"),
	PASSWORD_IS_NULL("Password is null"),
	PASSWORD_IS_EMPTY("Password is empty"),
	USERTYPE_IS_NULL("Usertype is null"),
	NOT_UNIQUE_USERNAME("Username is not unique");
	
	private String value;
	
	private UserErrorMessageKeys(String value) {
		this.value = value;
	}
	
	
	@Override
	public String getValue() {
		return value;
	}
	
}
