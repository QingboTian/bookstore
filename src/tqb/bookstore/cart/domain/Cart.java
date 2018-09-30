package tqb.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {
	private Map<String, CartItem> map = new HashMap<String, CartItem>();

	// 获得总计
	public double getTotal() {
		BigDecimal total = new BigDecimal("0");
		for (CartItem cartItem : map.values()) {
			BigDecimal subTotal = new BigDecimal(cartItem.getSubtotal());
			total = total.add(subTotal);
		}
		return total.doubleValue();
	}

	// 添加购物车的方法，参数为新的条目
	public void add(CartItem cartItem) {
		if (map.containsKey(cartItem.getBook().getBid())) {
			// 旧的条目
			CartItem _cartItem = map.get(cartItem.getBook().getBid());// 返回原来的条目
			_cartItem.setCount(_cartItem.getCount() + cartItem.getCount());// 更新旧的条目
			map.put(cartItem.getBook().getBid(), _cartItem);
		} else {
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}

	// 删除购物车
	public void delete(String bid) {
		map.remove(bid);
	}

	// 清空购物车
	public void clear() {
		map.clear();
	}

	// 获得总共的条目
	public Collection<CartItem> getCartItems() {
		return map.values();
	}
}
