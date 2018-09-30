package tqb.bookstore.user.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import tqb.bookstore.user.domain.User;

/**
 * Servlet Filter implementation class UserFilter
 */
@WebFilter(
		urlPatterns = { 
				"/jsps/order/*", 
				"/CartServlet",
				"/OrderServlet",
				"/jsps/cart/*"
		})
public class UserFilter implements Filter {

    public UserFilter() {
    }
	public void destroy() {
	}
	public void init(FilterConfig fConfig) throws ServletException {	
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		User user = (User) httpRequest.getSession().getAttribute("session_user");
		if (user != null){
			chain.doFilter(request, response);
		}else{
			httpRequest.setAttribute("msg", "请先登录再访问！");
			httpRequest.getRequestDispatcher("/jsps/user/login.jsp").forward(httpRequest, response);
		}
	}
	
	

}
