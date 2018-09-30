package tqb.bookstore.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.jdbc.TxQueryRunner;
import tqb.bookstore.category.domain.Category;

public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();

	public List<Category> findAll() {
		try {
			String sql = "select * from category";
			return qr.query(sql, new BeanListHandler<Category>(Category.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//删除操作
	public void delete(String cid) {
		try {
			String sql = "delete from category where cid=?";
			qr.update(sql, cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Category findByCname(String cname) {
		try {
			String sql = "select * from category where cname=?";
			return qr.query(sql, new BeanHandler<Category>(Category.class),cname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void add(Category category) {
		try {
			String sql = "insert into category values(?,?)";
			qr.update(sql, category.getCid(),category.getCname());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Category findByCid(String cid) {
		try {
			String sql = "select * from category where cid=?";
			return qr.query(sql, new BeanHandler<Category>(Category.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(Category form) {
		try {
			String sql = "update category set cname=? where cid=?";
			qr.update(sql,form.getCname(),form.getCid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
