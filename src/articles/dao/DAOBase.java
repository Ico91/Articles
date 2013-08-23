package articles.dao;

import org.apache.log4j.Logger;

import articles.database.transactions.TransactionManager;

public class DAOBase {
	protected TransactionManager manager;
	protected Logger logger;
	
	public DAOBase() {
		this.manager = new TransactionManager();
		this.logger = Logger.getLogger(getClass());
	}
}
