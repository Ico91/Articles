package articles.validators;

/**
 * Defines the error messages used while validating an user
 * 
 * @author Galina Hristova
 *
 */
public enum UserMessageKeys implements MessageKey {
	USERNAME_IS_NULL("USERNAME_IS_NULL"),
	USERNAME_IS_EMPTY("USERNAME_IS_EMPTY"),
	PASSWORD_IS_NULL("PASSWORD_IS_NULL"),
	PASSWORD_IS_EMPTY("PASSWORD_IS_EMPTY"),
	USERTYPE_IS_NULL("USERTYPE_IS_NULL"),
	NOT_UNIQUE_USERNAME("NOT_UNIQUE_USERNAME");
	
	private String value;
	
	private UserMessageKeys(String value) {
		this.value = value;
	}
	
	
	@Override
	public String getValue() {
		return value;
	}
	
}
