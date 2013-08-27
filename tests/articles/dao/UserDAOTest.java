package articles.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import articles.database.transactions.TransactionalTask;
import articles.model.User;
import articles.model.UserType;
import articles.model.dto.LoginRequest;
import articles.model.statistics.UserActivity;

public class UserDAOTest {
	private User actualUser;
	private LoginRequest loginRequest;
	private UserDAO userDAO;
	
	@Before
	public void setUp() {
		userDAO = new UserDAO();
		userDAO.manager.initTestManager();
		
		actualUser = new User(1, "admin", "123", UserType.ADMIN);
		
		userDAO.manager.execute(new TransactionalTask<Boolean>() {

			@Override
			public Boolean executeTask(EntityManager entityManager) {
				User user = new User();
				user.setUsername("admin");
				user.setPassword("123");
				user.setUserId(1);
				user.setUserType(UserType.ADMIN);
				entityManager.persist(user);

				return true;
			}

		});
	}
	
	@Test
	public void findTest() {
		loginRequest = new LoginRequest();
		loginRequest.setUsername("admin");
		loginRequest.setPassword("123");
		
		User expectedUser = userDAO.login(loginRequest.getUsername(), loginRequest.getPassword(), UserActivity.LOGIN, new Date());
		assertEquals(actualUser, expectedUser);
	}
	
	@After
    public void tearDown() throws Exception {
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
