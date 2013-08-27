package articles.model;


public enum UserType {
	ADMIN(0), USER(1);
	
	private int value;
	
	private UserType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
