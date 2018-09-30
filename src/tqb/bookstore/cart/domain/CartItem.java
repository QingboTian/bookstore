package tqb.bookstore.cart.domain;

import java.math.BigDecimal;

import tqb.bookstore.book.domain.Book;

public class CartItem {
	private Book book;
	private int count;

	// 获得小计
	public double getSubtotal() {
		// 这里要采用BigDecimal进行运算，防止出现二进制运算误差
		// 参数必须以字符串构造器给出
		BigDecimal price = new BigDecimal("" + book.getPrice());
		BigDecimal c = new BigDecimal("" + count);
		return price.multiply(c).doubleValue();
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "CartItem [book=" + book + ", count=" + count + "]";
	}

}
