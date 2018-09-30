package tqb.bookstore.admin.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import tqb.bookstore.admin.adminDao.Admin;

@WebFilter(
		urlPatterns = { "/adminjsps/admin/*",
				"/admin/AdminBookServlet", 
				"/admin/AdminCategoryServlet", 
				"/admin/AdminOrderServlet", 
				"/admin/AdminUserServlet", 
				"/admin/AdminUploadServlet"})
public class AdminFilter implements Filter {

    public AdminFilter() {
    }
    
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Admin admin = (Admin) httpRequest.getSession().getAttribute("session_admin");
		if (admin != null){
			chain.doFilter(request, response);
		}else{
			httpRequest.setAttribute("msg", "请登录再进行访问！");
			httpRequest.getRequestDispatcher("/adminjsps/login.jsp").forward(httpRequest, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
