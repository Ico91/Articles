package articles.web.resources.exception;

public class UserResourceException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserResourceException() {
		super();
	}

	public UserResourceException(String message) {
		super(message);
	}
}
