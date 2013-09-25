package articles.web.requests.users;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import articles.dao.UserDAO;
import articles.database.transactions.TransactionManager;
import articles.dto.ResultDTO;
import articles.model.User;
import articles.model.UserType;

public class UsersPageRequestTests {
	private static TransactionManager transactionManager = new TransactionManager();
	private static UserDAO userDAO;
	private static List<Integer> userIds;

	@BeforeClass
	public static void setUp() {
		userDAO = new UserDAO();
		transactionManager.initTestManager();
		userIds = new ArrayList<Integer>();
		for (int i = 0; i < 20; i++) {
			User user = new User();
			user.setUsername("TestUser" + i);
			user.setPassword("123");
			user.setUserType(UserType.USER);
			
			userIds.add(userDAO.addUser(user).getUserId());
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		for (int i : userIds)
			userDAO.deleteUser(i);
	}

	@Test
	public void testUsersPage() {
		int expectedSize = 5;
		String expectedFirstUsername = "TestUser0";
		String expectedLastUsername = "TestUser12";
		
		@SuppressWarnings("unchecked")
		ResultDTO<User> result = (ResultDTO<User>) new UsersPageRequest(0, 5, userDAO, "").process().getEntity();
		
		assertTrue(expectedSize == result.getResults().size());
		assertTrue(expectedFirstUsername.equals(result.getResults().get(0).getUsername()));
		assertTrue(expectedLastUsername.equals(result.getResults().get(4).getUsername()));
	}
}
