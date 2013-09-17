package articles.validators;

public enum PageMessageKeys implements MessageKey {
	NEGATIVE_FROM_PARAMETER("'From' parameter cannot be negative number"),
	NEGATIVE_TO_PARAMETER("'To' parameter cannot be negative number"),
	FROM_GREATER_THAN_TOTAL("'From' parameter is greater than total results"),
	TO_LOWER_THAN_FROM("'From' parameter is greater than 'To'"),
	FROM_EQUALS_TO("'From' parameter cannot be equal to 'To' parameter");
	private String value;
	
	private PageMessageKeys(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
