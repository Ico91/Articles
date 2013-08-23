package articles.database.transactions;

//TODO do we realy need this
public class TransactionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TransactionException(String message) {
		super(message);
	}
}
