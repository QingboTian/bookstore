package tqb.bookstore.order.domain;

import java.util.Date;
import java.util.List;

import tqb.bookstore.user.domain.User;

public class Order {
	private String oid;//订单编号
	private Date ordertime;//下单时间
	private double total;//总计
	private int state;//4种订单状态 1未付款 2付款未发货 3发货未收货 4收货成功
	private User owner;//订单所有者
	private String address;//收货地址

	private List<OrderItem> orderItemList;//关联订单条目

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	@Override
	public String toString() {
		return "OrderItem [oid=" + oid + ", ordertime=" + ordertime + ", total=" + total + ", state=" + state
				+ ", owner=" + owner + ", address=" + address + ", orderItemList=" + orderItemList + "]";
	}
}
