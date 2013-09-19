package articles.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import articles.model.UserType;
import articles.web.listener.ConfigurationListener;

/**
 * Filter that provides access to the administration panel for users of type
 * ADMIN only
 * 
 * @author Galina Hristova
 * 
 */
@WebFilter("/AdministratorFilter")
public class AdministratorFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(false);
		
		if(req.getMethod().equals("OPTIONS")) {
			chain.doFilter(req, resp);
			return;
		}
		
		if (session != null) {
			if ((UserType) session.getAttribute(ConfigurationListener.USERTYPE) == UserType.ADMIN) {
				chain.doFilter(req, resp);
				return;
			}
		}

		resp.sendError(403, "Unauthorized access");
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
