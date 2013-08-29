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

import org.apache.log4j.Logger;

/**
 * Generate after login the path from a property file which defines where the articles to be
 * stored.
 * 
 * @author Galina Hristova
 */
@WebListener
public class ConfigurationListener implements HttpSessionListener,
		ServletContextListener {
	private static final String CONFIGURATION_PATH = "WEB-INF/config.properties";
	public static final String USERID = "userId";
	public static final String USERTYPE = "userType";
	public static final String PERSISTENCE_NAME = "UserPE";
	public static final String PERSISTENCE_NAME_TEST = "TestPE";
	private static String path = "";
	private Logger logger = Logger.getLogger(getClass());

	@Context
	ServletContext context;

	public static String getPath() {
		if (path == null) {
			throw new RuntimeException("Cannot read articles directory path!");
		}

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
			properties.load(this.context
					.getResourceAsStream(CONFIGURATION_PATH));
			path = properties.getProperty("path") + "/";
		} catch (IOException e) {
			logger.error("Error while configuring the path.");
			throw new RuntimeException(e);
		}
	}

}
