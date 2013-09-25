package articles.dao.exceptions;

/**
 * Exception thrown by all DAO objects
 * 
 * @author Krasimir Atanasov
 * 
 */
public class DAOException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DAOException(String message) {
		super(message);
	}
}
