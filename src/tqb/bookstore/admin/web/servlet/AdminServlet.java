package tqb.bookstore.admin.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import tqb.bookstore.admin.adminDao.Admin;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("html/text;charset=utf-8");

		String adminname = request.getParameter("adminname");
		String password = request.getParameter("password");

		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("admin.properties");
		props.load(inputStream);
		System.out.println(props.getProperty("adminname") + "_" + props.getProperty("password"));
		String _adminname = props.getProperty("adminname");
		String _password = props.getProperty("password");

		Admin admin = new Admin();
		admin.setAdminname(_adminname);
		admin.setPassword(_password);

		if (adminname.equals(_adminname) && password.equals(_password)) {
			request.getSession().setAttribute("session_admin", admin);
			return "r:/adminjsps/admin/index.jsp";
		} else {
			request.setAttribute("msg", "账号或密码错误！");
			return "f:/adminjsps/login.jsp";
		}
	}
	
	public String exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		return "r:/adminjsps/login.jsp";
	}
}
