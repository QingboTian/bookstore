package tqb.bookstore.category.service;

import java.util.List;

import tqb.bookstore.book.dao.BookDao;
import tqb.bookstore.category.dao.CategoryDao;
import tqb.bookstore.category.domain.Category;
import tqb.bookstore.category.web.servlet.CategoryException;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	private BookDao bookDao = new BookDao();

	public List<Category> findAll() {
		return categoryDao.findAll();
	}

	//删除操作
	public void delete(String cid) throws CategoryException {
		//查询当前分类下的图书数量
		int count = bookDao.findCountByCid(cid);
		//如果当前分类下的图书数量大于0，则抛出异常
		if (count > 0){
			throw new CategoryException("当前分类下存在图书，禁止删除！");
		}
		//删除操作
		categoryDao.delete(cid);
	}

	public void add(Category form) throws CategoryException {
		// 通过cname查询分类图书
		String cname = form.getCname();
		Category category = categoryDao.findByCname(cname);
		
		if (category == null){
			//添加分类
			categoryDao.add(form);
		}else{
			throw new  CategoryException("该分类已存在，切勿重复添加！");
		}
	}

	public Category editPre(String cid) {
		return categoryDao.findByCid(cid);
	}
	
	public void edit(Category form) throws CategoryException {
		// 通过cname查询分类图书
		String cname = form.getCname();
		Category category = categoryDao.findByCname(cname);
		
		if (category == null){
			//更新分类名称
			categoryDao.update(form);
		}else{
			throw new  CategoryException("该分类已存在，请重新修改分类名称！");
		}
	}
}
