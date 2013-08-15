package articles.model.statistics;

public enum Event {
	LOGIN(1), LOGOUT(2), CREATE(3), MODIFY(4), DELETE(5);
	
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
