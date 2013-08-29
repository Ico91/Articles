package articles.model;

public enum UserActivity {
	LOGIN(1), LOGOUT(2), CREATE_ARTICLE(3), MODIFY_ARTICLE(4), DELETE_ARTICLE(5);

	private int value;

	private UserActivity(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
