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
public class SessionPathConfigurationListener implements HttpSessionListener, ServletContextListener {
	private static String path = "";
	@Context
	ServletContext context;

	public static String getPath() {
		return path;
	}
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		this.context = event.getServletContext();
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
    	Properties properties = new Properties();
		try {
			properties.load(this.context.getResourceAsStream("WEB-INF/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		path = properties.getProperty("path") + "/";
	}
	
}
