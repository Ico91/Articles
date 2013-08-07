package articles.dao;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
		Properties prop = new Properties();
		 
    	try {
               //load a properties file
    		prop.load(new FileInputStream("WebContent/WEB-INF/config.properties"));

	        //get the property value and print it out
	            System.out.println(prop.getProperty("path"));

	        } 
	    catch (IOException ex) {
	        ex.printStackTrace();
	    }
		User expectedUser = userDAO.find("admin", "123");
		assertEquals(actualUser, expectedUser);
		System.out.println(expectedUser.toString());
		System.out.println(actualUser.toString());
	}

}
