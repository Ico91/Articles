package articles.messages;

public enum ErrorRequestMessageKeys implements MessageKey {
	WRONG_LOGIN("WRONG_LOGIN"),
	LOGOUT_ERROR("LOGOUT_ERROR"),
	FORBIDDEN("FORBIDDEN");
	
	private String value;
	
	private ErrorRequestMessageKeys(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
}
