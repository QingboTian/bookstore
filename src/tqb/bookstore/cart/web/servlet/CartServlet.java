package tqb.bookstore.cart.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import tqb.bookstore.book.domain.Book;
import tqb.bookstore.book.service.BookService;
import tqb.bookstore.cart.domain.Cart;
import tqb.bookstore.cart.domain.CartItem;

@WebServlet("/CartServlet")
public class CartServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 购物车模块使用到Session 这里需要再登录功能修改代码添加购物车
		 */
		// 获得购物车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		// 获得bid
		String bid = request.getParameter("bid");
		// 获得count
		int count = Integer.parseInt(request.getParameter("count"));
		// 调用load方法获得Book对象
		Book book = new BookService().load(bid);
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		// 向购物车中添加条目
		cart.add(cartItem);
		return "f:/jsps/cart/list.jsp";
	}

	public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}

	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得购物车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		// 获得bid
		String bid = request.getParameter("bid");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}

}
