package articles.model.statistics;

public enum Event {
	LOGIN(1), LOGOUT(2), CREATE_ARTICLE(3), MODIFY_ARTICLE(4), DELETE_ARTICLE(5);
	
	private int value;
	
	private Event(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static Event getEvent(int value) {
		for(Event e : Event.values()) {
			if(e.value == value)
				return e;
		}
		throw new IllegalArgumentException("Invalid enum value");
	}
}
