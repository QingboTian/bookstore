package tqb.bookstore.category.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import tqb.bookstore.category.domain.Category;
import tqb.bookstore.category.service.CategoryService;
import tqb.bookstore.category.web.servlet.CategoryException;

@WebServlet("/admin/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private CategoryService categoryService = new CategoryService();

	/**
	 * 查询所有的分类
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";
	}

	/**
	 * 删除分类操作 这里删除的分类必须是一个空分类，即没有相应图书的分类
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得cid
		String cid = request.getParameter("cid");
		try {
			categoryService.delete(cid);
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			// 转发到msg.jsp
			return "f:/adminjsps/msg.jsp";
		}
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加分类功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//封装表单数据
		Category form = CommonUtils.toBean(request.getParameterMap(), Category.class);
		//补全数据
		form.setCid(CommonUtils.uuid());
		try {
			categoryService.add(form);
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			// 转发到msg.jsp
			return "f:/adminjsps/msg.jsp";
		}
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 修改分类名称前的加载操作
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获得cid
		String cid = request.getParameter("cid");
		Category category = categoryService.editPre(cid);
		request.setAttribute("category", category);
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	
	/**
	 * 修改分类名称功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category form = CommonUtils.toBean(request.getParameterMap(), Category.class);
		try {
			categoryService.edit(form);
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			// 转发到msg.jsp
			return "f:/adminjsps/msg.jsp";
		}
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";
	}

}
