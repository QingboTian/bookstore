package tqb.bookstore.user.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import tqb.bookstore.user.domain.User;
import tqb.bookstore.user.service.UserException;
import tqb.bookstore.user.service.UserService;

@WebServlet("/admin/AdminUserServlet")
public class AdminUserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();
	
	/**
	 * 查看所有用户
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<User> userList = userService.findAll();
		request.setAttribute("userList", userList);
		return "f:/adminjsps/admin/user/list.jsp";
	}
	
	/**
	 * 修改密码之前的加载工作
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("uid");
		User user = userService.editPre(uid);
		request.setAttribute("user", user);
		return "f:/adminjsps/admin/user/mod.jsp";
	}

	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = CommonUtils.toBean(request.getParameterMap(), User.class);
		try {
			userService.edit(user);
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", user);
			return "f:/adminjsps/admin/user/mod.jsp";
		}
		return findAll(request, response);
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("uid");
		userService.delete(uid);
		return findAll(request, response);
	}
}
