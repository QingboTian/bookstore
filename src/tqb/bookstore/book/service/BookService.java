package tqb.bookstore.book.service;

import java.util.List;

import tqb.bookstore.book.dao.BookDao;
import tqb.bookstore.book.domain.Book;

public class BookService {
	private BookDao bookDao = new BookDao();
	
	//查询所有
	public List<Book> findAll() {
		return bookDao.findAll();
	}

	//按分类查询
	public List<Book> findByCategory(String cid) {
		return bookDao.findByCategory(cid);
	}

	//加载图书详细信息
	public Book load(String bid) {
		return bookDao.load(bid);
	}

	//添加图书
	public void add(Book book) {
		bookDao.add(book);
	}

	public void delete(String bid) {
		bookDao.delete(bid);
	}

	public void modify(Book book) {
		bookDao.modify(book);	
	}

}
