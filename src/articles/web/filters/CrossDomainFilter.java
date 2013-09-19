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

import org.apache.log4j.Logger;

public class CrossDomainFilter implements Filter {

	private final Logger logger;
	
	public CrossDomainFilter() {
		this.logger = Logger.getLogger(getClass());
	}
	
	@Override
	public void destroy() {
		//	Empty
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		
		String origin = req.getHeader("origin");
		
		this.logger.info("Request from: " + origin + " to URI:" + req.getRequestURI());
		
		resp.addHeader("Access-Control-Allow-Origin", req.getHeader("origin"));
		resp.addHeader("Access-Control-Allow-Headers",
				"origin, content-type, accept, authorization");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.addHeader("Access-Control-Allow-Methods",
				"GET, POST, PUT, DELETE, OPTIONS, HEAD");
		resp.addHeader("Access-Control-Max-Age", "1209600");

		resp.addHeader("p3p", "CP=\"Allow p3p\"");
		
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		//	Empty
	}

}
