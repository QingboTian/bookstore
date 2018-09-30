package tqb.bookstore.book.web.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;
import tqb.bookstore.book.domain.Book;
import tqb.bookstore.book.service.BookService;
import tqb.bookstore.category.domain.Category;
import tqb.bookstore.category.service.CategoryService;

/**
 * 只负责添加图书
 * 
 * @author tqb
 *
 */
@WebServlet("/admin/AdminUploadServlet")
public class AdminUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 处理编码问题
		request.setCharacterEncoding("utf-8");
		response.setContentType("html/text;charset=utf-8");

		// 创建工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 创建解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		
		//设置上传文件大小(50k)
		sfu.setFileSizeMax(50 * 1024);
		
		// 解析请求
		try {
			List<FileItem> fileItemList = sfu.parseRequest(request);
			
			/**
			 * 表单验证
			 */
			if (fileItemList.get(0).getString("utf-8").trim().equals("")){
				request.setAttribute("msg", "图书名称不能为空!");
				// 获得分类列表
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			
			if (fileItemList.get(2).getString("utf-8").trim().equals("")){
				request.setAttribute("msg", "图书单价不能为空!");
				// 获得分类列表
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			
			if (fileItemList.get(3).getString("utf-8").trim().equals("")){
				request.setAttribute("msg", "图书作者不能为空!");
				// 获得分类列表
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			
			// 将集合中解析的数据封装到Map中
			Map<String, String> map = new HashMap<String, String>();
			for (FileItem fileItem : fileItemList) {
				
				// 如果不是文件表单
				if (fileItem.isFormField()) {
					map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
				}
			}
			// 继续封装到book中
			Book book = CommonUtils.toBean(map, Book.class);
			//封装到Category中
			Category category = CommonUtils.toBean(map, Category.class);
			// 补全bid
			book.setBid(CommonUtils.uuid());
			// 补全cid
			book.setCategory(category);
			// 获得存储图书图片文件的目录
			String realPath = this.getServletContext().getRealPath("/book_img");
			// 设置文件的名称
			String filename = CommonUtils.uuid() + "_" + fileItemList.get(1).getName();
			
			//判断文件是否是jpg格式
			if (!filename.toLowerCase().endsWith("jpg") && !filename.toLowerCase().endsWith("jpeg")){
				request.setAttribute("msg", "您上传的图片不是jpg或jpeg格式，请重新选择上传！");
				// 获得分类列表
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			
			// 保存文件
			File file = new File(realPath, filename);
			fileItemList.get(1).write(file);
			
			//校验图片尺寸
			Image image = new ImageIcon(file.getAbsolutePath()).getImage();
			if (image.getWidth(null) > 120 || image.getHeight(null) > 150){
				request.setAttribute("msg", "您上传的图片尺寸请保持在120*150之间，请重新选择上传！");
				// 获得分类列表
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
				//删除文件
				file.delete();
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			
			// 获得image
			String imageStr = "book_img/" + filename;
			book.setImage(imageStr);
			
			// 添加图书
			bookService.add(book);
			// 转发list下
			List<Book> bookList = bookService.findAll();
			request.setAttribute("bookList", bookList);
			request.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(request, response);
		} catch (Exception e) {
			if (e instanceof FileSizeLimitExceededException){
				request.setAttribute("msg", "您上传的图片大小超过了50k，请重新上传！");
				// 获得分类列表
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
			}
		}
	}

}
