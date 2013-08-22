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

/**Generate the path from a property file which defines where the articles to be stored.
 * Author Galina Hristova
 * Application Lifecycle Listener implementation class SessionAttributesConfigurationListener
 *
 */
@WebListener
public class ConfigurationListener implements HttpSessionListener, ServletContextListener {
	private static String path = "";
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
    	Properties properties = new Properties();
		try {
			properties.load(this.context.getResourceAsStream("WEB-INF/config.properties"));
			path = properties.getProperty("path") + "/";
		} catch (IOException e) {
			path = null;
		}
		
		
	}
	
}