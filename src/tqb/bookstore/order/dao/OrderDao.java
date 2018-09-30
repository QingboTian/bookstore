package tqb.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import tqb.bookstore.book.domain.Book;
import tqb.bookstore.order.domain.Order;
import tqb.bookstore.order.domain.OrderItem;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();

	// 添加订单
	public void addOrder(Order order) {
		try {
			// 这里要处理一下下单的时间，将utils下date转为sql下的date
			Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
			String sql = "insert into orders values(?,?,?,?,?,?)";
			Object[] params = { order.getOid(), timestamp, order.getTotal(), order.getState(),
					order.getOwner().getUid(), order.getAddress() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 添加条目
	// 采用批处理进行添加数据
	public void addOrderItemList(List<OrderItem> orderItemList) {
		try {
			String sql = "insert into orderitem values(?,?,?,?,?)";
			// 创建二维数组
			Object[][] params = new Object[orderItemList.size()][];
			// 循环遍历给二维数组赋值
			for (int i = 0; i < orderItemList.size(); i++) {
				OrderItem item = orderItemList.get(i);
				params[i] = new Object[] { item.getIid(), item.getCount(), item.getSubtotal(), item.getOrder().getOid(),
						item.getBook().getBid() };
			}
			qr.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 我的订单
	 * 通过用户id查询订单
	 * 
	 * @param uid
	 * @return
	 */
	public List<Order> findByUid(String uid) {
		try {
			String sql = "select * from orders where uid=? order by state";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), uid);

			// 循环遍历订单列表
			for (Order order : orderList) {
				// 通过订单加载订单条目
				List<OrderItem> loadOrderItems = loadOrderItems(order);
				order.setOrderItemList(loadOrderItems);
			}

			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过订单查询订单条目
	 * 
	 * @param order
	 * @throws SQLException
	 */
	private List<OrderItem> loadOrderItems(Order order) throws SQLException {
		// 通过订单号查询订单条目
		// 这里查询两张表的内容 book和orderitem
		String sql = "select * from orderitem i,book b where i.bid=b.bid and oid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
		// 遍历map集合去生成两个对象
		// 分别是Book和OrderItem
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		return orderItemList;
	}

	/**
	 * 将Map集合遍历去生成OrderItem对象
	 * 
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		// 循环遍历
		for (Map<String, Object> map : mapList) {
			// 把一个Map转换成一个OrderItem对象
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}

	/**
	 * 通过Map得到Book的值并赋值给OrderItem
	 * 
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		// System.out.println(book);
		orderItem.setBook(book);
		return orderItem;
	}

	public Order load(String oid) {
		try {
			String sql = "select * from orders where oid=?";
			Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);

			List<OrderItem> loadOrderItems = loadOrderItems(order);
			order.setOrderItemList(loadOrderItems);

			return order;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过oid查询订单状态
	 * @param oid
	 * @return
	 */
	public int getStateByOid(String oid){
		try{
			String sql = "select state from orders where oid=?";
			Number state = (Number)qr.query(sql, new ScalarHandler(), oid);
			return state.intValue();
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public void updateState(int state, String oid){
		try{
			String sql = "update orders set state=? where oid=?";
			qr.update(sql, state, oid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	//查询所有订单
	public List<Order> findAll() {
		try {
			String sql = "select * from orders";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class));

			// 循环遍历订单列表
			for (Order order : orderList) {
				// 通过订单加载订单条目
				List<OrderItem> loadOrderItems = loadOrderItems(order);
				order.setOrderItemList(loadOrderItems);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Order> findByState(String state) {
		try {
			String sql = "select * from orders where state=?";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class),state);

			// 循环遍历订单列表
			for (Order order : orderList) {
				// 通过订单加载订单条目
				List<OrderItem> loadOrderItems = loadOrderItems(order);
				order.setOrderItemList(loadOrderItems);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
