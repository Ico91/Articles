package articles.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import articles.model.User;
import articles.model.UserActivity;
import articles.model.UserType;
import articles.model.dto.LoginRequest;

public class UserDAOTest {
	private User actualUser;
	private LoginRequest loginRequest;
	private UserDAO userDAO;
	@Before
	public void setUp() {
		userDAO = new UserDAO();
		loginRequest = new LoginRequest("admin", "123");
		actualUser = new User(1, "admin", "123", UserType.USER);
	}
	
	@Test
	public void findTest() {
		User expectedUser;
		expectedUser = userDAO.login(loginRequest.getUsername(), loginRequest.getPassword(), UserActivity.LOGIN);
		assertEquals(actualUser.getUsername(), expectedUser.getUsername());
		System.out.println(expectedUser.toString());
		System.out.println(actualUser.toString());
	}
	
	@Test
	public void updateLastLoginTest() {
		int userId = 1;
		Date lastLogin = new Date();
		userDAO.updateLastLogin(lastLogin, userId);
		//System.out.println(userDAO.getUser(loginRequest.getUsername(), loginRequest.getPassword()).getLastLogin(), null);
		
	}

}
