package articles.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import articles.database.transactions.TransactionManager;
import articles.database.transactions.TransactionalTask;
import articles.dto.LoginRequest;
import articles.model.User;
import articles.model.UserActivity;
import articles.model.UserType;

public class UserDAOTest {
	private static User actualUser;
	private static TransactionManager transactionManager = new TransactionManager();
	private static UserDAO userDAO;
	
	@BeforeClass
	public static void setUp() {
		transactionManager.initTestManager();
		userDAO = new UserDAO(transactionManager);
		
		actualUser = new User(3, "admin", "123", UserType.ADMIN);
		
		userDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				User user = new User();
				user.setUsername("admin");
				user.setPassword("123");
				user.setUserId(3);
				user.setUserType(UserType.ADMIN);
				entityManager.persist(user);

				return true;
			}

		});
	}
	
	@Test
	public void loginTest() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("admin");
		loginRequest.setPassword("123");
		
		User expectedUser = userDAO.login(loginRequest.getUsername(), loginRequest.getPassword(), UserActivity.LOGIN, new Date());
		assertEquals(actualUser, expectedUser);
	}
	
	@Test
	public void addUserTest() {
		User newUser = new User();
		newUser.setUsername("guest");
		newUser.setPassword("122");
		newUser.setUserType(UserType.USER);
		
		User user = userDAO.addUser(newUser);
		List<User> users = userDAO.getUsers();
		String expectedUsername = null;
		for (User u : users) {
			if (u.getUsername().equals(user.getUsername())) {
				expectedUsername = u.getUsername();
			}
		}
		
		assert(expectedUsername.equals(user.getUsername()));
	}
	
	
	
	@Test
	public void updateUser() {
		User updateUser = new User();
		updateUser.setUsername("admin");
		updateUser.setPassword("222");
		updateUser.setUserType(UserType.ADMIN);
		
		userDAO.updateUser(3, updateUser);
		User expected = userDAO.getUserById(3);
		
		assert(expected.getPassword().equals(updateUser.getPassword()));
		System.out.println(userDAO.getUserById(3) + " update");
	}
	
	@Test
	public void getUserByIdTest() {
		System.out.println(userDAO.getUserById(3));
	}
	
	@Test
	public void deleteTest() {
		userDAO.deleteUser(3);
		
		assert(userDAO.getUserById(3).getUsername().equals("admin"));
		System.out.println(userDAO.getUsers());
	}
	
	@AfterClass
    public static void tearDown() throws Exception {
		userDAO.manager.execute(new TransactionalTask<Boolean>() {
			@Override
			public Boolean executeTask(EntityManager entityManager) {
				
				Query deleteQuery = entityManager
						.createNativeQuery("DROP TABLE users");
				deleteQuery.executeUpdate();
				return true;
			}

		});
    }

}
