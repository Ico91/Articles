package articles.validators;

public enum ArticleMessageKeys implements MessageKey{
	
	TITLE_IS_NULL("TITLE_IS_NULL"),
	TITLE_IS_EMPTY("TITLE_IS_EMPTY"),
	CONTENT_IS_NULL("CONTENT_IS_NULL"),
	CONTENT_IS_EMPTY("CONTENT_IS_EMPTY"),
	NOT_UNIQUE_TITLE("NOT_UNIQUE_TITLE");
	
	private String value;
	
	private ArticleMessageKeys(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
}
