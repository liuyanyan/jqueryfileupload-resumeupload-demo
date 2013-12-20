package org.lyy.file;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if(((HttpServletRequest)request).getMethod().equalsIgnoreCase("OPTIONS")){
			((HttpServletResponse)response).addHeader("Access-Control-Allow-Origin", "*"); // set headers for cross-domain
			((HttpServletResponse)response).setHeader("Access-Control-Allow-Headers","Content-Type, Content-Range, Content-Disposition");
			//((HttpServletResponse)response).setHeader("Access-Control-Allow-Credentials","true");
		}
		chain.doFilter(request, response);
	}


	@Override
	public void init(FilterConfig arg) throws ServletException {
		// TODO Auto-generated method stub
	}

}
