package tqb.bookstore.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;
import tqb.bookstore.cart.domain.Cart;
import tqb.bookstore.user.domain.User;
import tqb.bookstore.user.service.UserException;
import tqb.bookstore.user.service.UserService;

@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();

	// 用户注册
	public String regist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 封装表单数据
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		// 补全User
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());

		// 使用Map校验表单数据
		Map<String, String> errors = new HashMap<String, String>();
		// 验证用户名
		String username = request.getParameter("username");
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名长度为3-10位");
		}
		// 验证密码
		String password = request.getParameter("password");
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码长度为3-10位");
		}
		// 验证邮箱
		String email = request.getParameter("email");
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "邮箱不能为空");
		} else if (!email.matches("\\w+@\\w+.\\w+")) {// 正则验证
			errors.put("email", "邮箱格式错误");
		}
		// 判断是否存在错误
		if (errors.size() > 0) {
			// 如果存在错误，在request保存错误信息
			// 为了回显，也保存form
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			// 转发到注册页面
			return "f:/jsps/user/regist.jsp";
		}

		// 调用regist方法
		try {
			userService.regist(form);
		} catch (UserException e) {
			// 保存出错信息
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			// 转发到注册页面
			return "f:/jsps/user/regist.jsp";
		}

		/**
		 * 发送激活邮件
		 */
		Properties props = new Properties();
		// 加载配置文件
		props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		String host = props.getProperty("host");
		String uname = props.getProperty("uname");
		String pwd = props.getProperty("pwd");
		Session session = MailUtils.createSession(host, uname, pwd);

		String from = props.getProperty("from");
		String subject = props.getProperty("subject");
		// 更换占位符{0}{1}为激活码
		String content = props.getProperty("content");
		content = MessageFormat.format(content, form.getCode(), form.getCode());
		String to = form.getEmail();
		Mail mail = new Mail(from, to, subject, content);

		// 发送邮件
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			// 发送邮件也可能发生错误
		}
		request.setAttribute("msg", "恭喜你注册成功，请马上去邮箱激活！");
		return "f:/jsps/msg.jsp";
	}

	// 激活账户
	public String active(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");

		String code = request.getParameter("code");
		try {
			userService.active(code);
			request.setAttribute("msg", "恭喜你，账号激活成功，立即登录！");
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}

	// 登录
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 封装表单数据
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		// 校验表单数据
		// 使用Map校验表单数据
		Map<String, String> errors = new HashMap<String, String>();
		// 验证用户名
		String username = request.getParameter("username");
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "请输入用户名");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名长度必须为3-10位");
		}
		// 验证密码
		String password = request.getParameter("password");
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码长度必须为3-10位");
		}
		// 判断是否存在错误
		if (errors.size() > 0) {
			// 如果存在错误，在request保存错误信息
			// 为了回显，也保存form
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			// 转发到注册页面
			return "f:/jsps/user/login.jsp";
		}

		// 调用登录方法
		try {
			User user = userService.login(form);
			// 将用户信息保存到Session中
			request.getSession().setAttribute("session_user", user);
			//创建购物车
			request.getSession().setAttribute("cart", new Cart());
			// 重定向到index
			return "r:/index.jsp";
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
	}
	
	// 账号退出
	public String exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//销毁当前Session
		request.getSession().invalidate();
		//转发到index.jsp
		return "r:/index.jsp";
	}

}
