package articles.model;


public enum UserType {
	ADMIN(1), USER(2);
	
	//TODO why ?
	private int value;
	
	private UserType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	//TODO why ?
	public static UserType getType(int value) {
		for(UserType e : UserType.values()) {
			if(e.value == value)
				return e;
		}
		throw new IllegalArgumentException("Invalid enum value");
	}
}
