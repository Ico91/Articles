package articles.web.listener;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.ws.rs.core.Context;

//TODO comment structure ? 
/**Generate the path from a property file which defines where the articles to be stored.
 * Application Lifecycle Listener implementation class SessionAttributesConfigurationListener
 *
 *@author Galina Hristova
 */
@WebListener
public class ConfigurationListener implements HttpSessionListener, ServletContextListener {
	private static String path = "";
	private static final String CONFIGURATION_PATH = "WEB-INF/config.properties";
	public static final String USERID = "userId";
	public static final String PERSISTENCE_NAME = "UserPE";
	public static final String PERSISTENCE_NAME_TEST = "UserPE";
	
	@Context
	ServletContext context;

	public static String getPath() {
		return path;
	}
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		this.context = event.getServletContext();
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		
	}
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		try { 
			Properties properties = new Properties();
			properties.load(this.context.getResourceAsStream(CONFIGURATION_PATH));
			path = properties.getProperty("path") + "/";	
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		}
	}
	
}
