package articles.validators;

public enum PaginationMessageKeys implements MessageKey {
	NEGATIVE_FROM_PARAMETER("'From' parameter cannot be negative number"),
	NEGATIVE_TO_PARAMETER("'To' parameter cannot be negative number"),
	FROM_GREATER_THAN_TO("'To' parameter is greater than 'From'"),
	FROM_GREATER_THAN_TOTAL("'From' parameter is greater than total results"),
	FROM_EQUALS_TO("'From' parameter cannot be equal to 'To' parameter");
	private String value;
	
	private PaginationMessageKeys(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
