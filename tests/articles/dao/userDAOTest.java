package articles.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import articles.model.User;

public class userDAOTest {
	private User actualUser;
	private UserDAO userDAO;
	@Before
	public void setUp() throws Exception {
		userDAO = new UserDAO();
		actualUser = new User(1, "admin", "123");
	}
	
	@Test
	public void test() {
		User expectedUser = userDAO.find("admin", "123");
		assertEquals(actualUser, expectedUser);
		System.out.println(expectedUser.toString());
		System.out.println(actualUser.toString());
	}

}
