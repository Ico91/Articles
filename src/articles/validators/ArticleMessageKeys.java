package articles.validators;

public enum ArticleMessageKeys implements MessageKey{
	
	TITLE_IS_NULL("Article title is null"),
	TITLE_IS_EMPTY("Article title is empty"),
	CONTENT_IS_NULL("Article content is null"),
	CONTENT_IS_EMPTY("Article content is empty"),
	NOT_UNIQUE_TITLE("Article title must be unique");
	
	private String value;
	
	private ArticleMessageKeys(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
}
