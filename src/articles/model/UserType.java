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
	
	public static UserType getType(int value) {
		for(UserType e : UserType.values()) {
			if(e.value == value)
				return e;
		}
		throw new IllegalArgumentException("Invalid enum value");
	}
}
