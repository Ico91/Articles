import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;

public class UsersResourceTest extends JerseyTest {
	public static final String PACKAGE_NAME = "articles.web.resources";
	
	public UsersResourceTest()throws Exception {
        super(PACKAGE_NAME);
    }

    @Test
    public void testUserLogin() {
        WebResource webResource = resource();
        Response responseMsg = webResource.path("users/login").get(Response.class);
        assertEquals("Hello World", responseMsg);
    }

}
