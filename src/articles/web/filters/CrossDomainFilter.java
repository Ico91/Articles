package articles.web.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CrossDomainFilter implements Filter {

	@Override
	public void destroy() {
		// FIXME Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		
		System.out.println(req.getHeader("origin") + " asdasdasd");
		
		resp.addHeader("Access-Control-Allow-Origin", req.getHeader("origin"));
		resp.addHeader("Access-Control-Allow-Headers",
				"origin, content-type, accept, authorization");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.addHeader("Access-Control-Allow-Methods",
				"GET, POST, PUT, DELETE, OPTIONS, HEAD");
		resp.addHeader("Access-Control-Max-Age", "1209600");
		System.out.println("CrossDomainFilter from: " + req.getRequestURI());
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// FIXME Auto-generated method stub

	}

}
