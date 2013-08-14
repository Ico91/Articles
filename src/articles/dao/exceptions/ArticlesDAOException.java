package articles.dao.exceptions;

/**
 * Exception thrown by ArticlesDAO
 * 
 * @author Krasimir Atanasov
 * 
 */
public class ArticlesDAOException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs new ArticlesDAOException object with specified exception
	 * message
	 * 
	 * @param message
	 *            Exception message
	 */
	public ArticlesDAOException(String message) {
		super(message);
	}
}
