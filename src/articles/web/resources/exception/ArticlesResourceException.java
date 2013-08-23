package articles.web.resources.exception;

//TODO do we really need this ?
public class ArticlesResourceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ArticlesResourceException() {
		super();
	}

	public ArticlesResourceException(String message) {
		super(message);
	}
}
