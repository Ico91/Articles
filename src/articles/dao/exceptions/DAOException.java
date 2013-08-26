package articles.dao.exceptions;

/**
 * Exception thrown by ArticlesDAO
 * 
 * @author Krasimir Atanasov
 * 
 */
public class DAOException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs new ArticlesDAOException object with specified exception
	 * message
	 * 
	 * @param message
	 *            Exception message
	 */
	public DAOException(String message) {
		super(message);
	}
}
