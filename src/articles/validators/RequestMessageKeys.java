package articles.validators;

public enum RequestMessageKeys implements MessageKey{
	ARTICLE_ADDED("ARTICLE_ADDED"),
	ARTICLE_UPDATED("ARTICLE_UPDATED"),
	ARTICLE_DELETED("ARTICLE_DELETED"),
	USER_ADDED("USER_ADDED"),
	USER_UPDATED("USER_UPDATED"),
	USER_DELETED("USER_DELETED"),
	USER_CANNOT_DELETE_HIMSELF("USER_CANNOT_DELETE_HIMSELF");
	
	private String value;
	
	private RequestMessageKeys(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
	
}
