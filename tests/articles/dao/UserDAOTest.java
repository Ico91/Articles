package articles.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.UserActivity;
import articles.model.UserType;
import articles.model.dto.LoginRequest;
import articles.model.dto.UserDetails;

public class UserDAOTest {
	private static User actualUser;
	private static UserDAO userDAO;
	
	@BeforeClass
	public static void setUp() {
		userDAO = new UserDAO();
		userDAO.manager.initTestManager();
		
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
	public void findTest() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("admin");
		loginRequest.setPassword("123");
		
		User expectedUser = userDAO.login(loginRequest.getUsername(), loginRequest.getPassword(), UserActivity.LOGIN, new Date());
		assertEquals(actualUser, expectedUser);
	}
	
	@Test
	public void addUserTest() {
		UserDetails newUser = new UserDetails("guest", "122", UserType.USER);
		User user = userDAO.addUser(newUser);
		System.out.println(user);
	}
	
	
	
	@Test
	public void updateUser() {
		UserDetails updateUser = new UserDetails("admin", "222", UserType.ADMIN);
		userDAO.updateUser(3, updateUser);
		System.out.println(userDAO.getUserById(3) + " update");
	}
	
	@Test
	public void getUserByIdTest() {
		System.out.println(userDAO.getUserById(3));
	}
	
	@Test
	public void deleteTest() {
		userDAO.deleteUser(3);
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
