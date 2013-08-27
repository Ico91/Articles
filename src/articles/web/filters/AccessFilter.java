package articles.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import articles.web.listener.ConfigurationListener;

public class AccessFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(false);

		if ( session != null && session.getAttribute(ConfigurationListener.USERID) != null ) {
			chain.doFilter(req, resp);
			return;	
		}

		resp.sendError(403, "Unauthorized access");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
