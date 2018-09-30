package tqb.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import tqb.bookstore.order.dao.OrderDao;
import tqb.bookstore.order.domain.Order;

public class OrderService {
	private OrderDao orderDao = new OrderDao();

	/**
	 * 添加操作 这里必须进行事务的处理 因为该方法需要操作两个表，防止其中一个表插入数据失败，另一个表插入数据成功，造成数据混乱
	 */
	public void add(Order order) {
		try {
			// 开启事务
			JdbcUtils.beginTransaction();
			// 具体操作
			orderDao.addOrder(order);
			orderDao.addOrderItemList(order.getOrderItemList());
			// 关闭事务
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			// 回滚事务
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		}
	}

	// 查询订单列表
	public List<Order> findByUid(String uid) {
		return orderDao.findByUid(uid);
	}
	
	// 加载订单
	public Order load(String oid) {
		return orderDao.load(oid);
	}
	
	// 确认收获
	public void confirm(String oid) throws OrderException{
		int state = orderDao.getStateByOid(oid);
		// 若订单状态不是3状态，确认收货失败
		if (state != 3){
			throw new OrderException("订单确认收货失败，且您的操作比较可疑，停止当前操作，否则将负刑事责任！");
		}
		// 收货成功
		orderDao.updateState(4, oid);
	}

	//付款
	public void zhiFu(String r6_Order) {
		//得到订单状态
		int state = orderDao.getStateByOid(r6_Order);
		//判断订单状态是否为1
		if (state == 1){
			//更新状态为2
			orderDao.updateState(2, r6_Order);
		}
	}

	//查看所有订单
	public List<Order> findAll() {
		return orderDao.findAll();
	}

	//发货
	public void send(String oid) throws OrderException {
		//返回当前订单状态
		int state = orderDao.getStateByOid(oid);
		if (state != 2){
			throw new OrderException("订单发货失败！");
		}
		orderDao.updateState(3, oid);
	}

	public List<Order> findByState(String state) {
		return orderDao.findByState(state);
	}
	
}
