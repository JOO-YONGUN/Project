package com.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * <code>CrossScriptingFilter</code> XSS Filter
 * 
 * @version $Revision: 0.1 $ $Date: 0000-00-00 00:00:00 $
 * @author 
 * @history
 */
public class CrossScriptingFilter implements Filter {

	public FilterConfig filterConfig;

	public CrossScriptingFilter() {
		filterConfig = null;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
	}

}
