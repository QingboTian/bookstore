package tqb.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import tqb.bookstore.book.domain.Book;
import tqb.bookstore.category.domain.Category;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();

	// 查询所有
	public List<Book> findAll() {
		try {
			String sql = "select * from book where del=false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 按分类查询
	public List<Book> findByCategory(String cid) {
		try {
			String sql = "select * from book where cid=? and del=false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 加载图书
	public Book load(String bid) {
		try {
			String sql = "select * from book where bid=?";
			Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
			Book book = CommonUtils.toBean(map, Book.class);
			Category category = CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			return book;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 查询当前分类下的图书本数
	public int findCountByCid(String cid) {
		try {
			String sql = "select count(*) from book where cid=?";
			Number count = (Number) qr.query(sql, new ScalarHandler(), cid);
			return count.intValue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void add(Book book) {
		try {
			String sql = "insert into book values(?,?,?,?,?,?)";
			Object[] params = { book.getBid(), book.getBname(), book.getPrice(), book.getAuthor(), book.getImage(),
					book.getCategory().getCid() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 假删除
	public void delete(String bid) {
		try {
			String sql = "update book set del=true where bid=?";
			qr.update(sql, bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void modify(Book book) {
		try {
			String sql = "update book set bname=?, price=?, author=?, cid=? where bid=?";
			Object[] params = { book.getBname(), book.getPrice(), book.getAuthor(), book.getCategory().getCid(),
					book.getBid() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
