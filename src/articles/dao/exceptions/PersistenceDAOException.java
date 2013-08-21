package articles.dao.exceptions;

public class PersistenceDAOException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PersistenceDAOException(String message) {
		super(message);
	}
}
