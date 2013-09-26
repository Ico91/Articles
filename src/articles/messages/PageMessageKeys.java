package articles.messages;

public enum PageMessageKeys implements MessageKey {
	NEGATIVE_FROM_PARAMETER("NEGATIVE_FROM_PARAMETER"),
	NEGATIVE_TO_PARAMETER("NEGATIVE_TO_PARAMETER"),
	FROM_GREATER_THAN_TOTAL("FROM_GREATER_THAN_TOTAL"),
	TO_LOWER_THAN_FROM("TO_LOWER_THAN_FROM"),
	FROM_EQUALS_TO("FROM_EQUALS_TO");
	
	private String value;
	
	private PageMessageKeys(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
