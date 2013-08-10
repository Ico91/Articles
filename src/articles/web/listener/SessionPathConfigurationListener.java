package articles.web.listener;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.ws.rs.core.Context;

/**
 * Author Galina
 * Application Lifecycle Listener implementation class SessionAttributesConfigurationListener
 *
 */
@WebListener
public class SessionPathConfigurationListener implements HttpSessionAttributeListener, ServletContextListener {
	private static String path = "";
	@Context
	ServletContext context;

	public static String getPath() {
		return path;
	}
	
	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {
        // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent event) {
        // TODO Auto-generated method stub
    	Properties properties = new Properties();
		try {
			properties.load(this.context.getResourceAsStream("WEB-INF/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		path = properties.getProperty("path") + "/" + event.getValue();
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent event) {
        // TODO Auto-generated method stub
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
	
}
