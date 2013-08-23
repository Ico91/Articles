package articles.model;


public enum UserType {
	ADMIN(1), USER(2);
	
	private int value;
	
	private UserType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
