package tqb.bookstore.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import tqb.bookstore.cart.domain.Cart;
import tqb.bookstore.cart.domain.CartItem;
import tqb.bookstore.order.domain.Order;
import tqb.bookstore.order.domain.OrderItem;
import tqb.bookstore.order.service.OrderException;
import tqb.bookstore.order.service.OrderService;
import tqb.bookstore.user.domain.User;

@WebServlet("/OrderServlet")
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private OrderService orderService = new OrderService();

	// 添加订单
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 从Session中获得购物车
		 */
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		/**
		 * 创建Order
		 */
		Order order = new Order();
		/**
		 * 向order中添加数据
		 */
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		// 订单状态为未付款
		order.setState(1);
		// 获得用户对象
		User user = (User) request.getSession().getAttribute("session_user");
		order.setOwner(user);
		order.setTotal(cart.getTotal());
		// 创建订单条目集合
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		// 循环遍历购物车条目
		for (CartItem cartItem : cart.getCartItems()) {
			// 创建订单条目
			OrderItem oi = new OrderItem();
			oi.setIid(CommonUtils.uuid());
			oi.setSubtotal(cartItem.getSubtotal());
			oi.setOrder(order);
			oi.setCount(cartItem.getCount());
			oi.setBook(cartItem.getBook());
			// 添加
			orderItemList.add(oi);
		}

		// 将订单条目添加到订单中
		order.setOrderItemList(orderItemList);
		// 调用Service方法
		orderService.add(order);
		// 清空购物车
		cart.clear();
		// 将order保存到request域中
		request.setAttribute("order", order);
		// 转发到order目录下的desc.jsp
		return "f:/jsps/order/desc.jsp";
	}

	/**
	 * 我的订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 从Session中获得User对象 从User中获得uid 通过uid查询所有订单
		 * 
		 */
		User user = (User) request.getSession().getAttribute("session_user");
		String uid = user.getUid();
		List<Order> orderList = orderService.findByUid(uid);
		// 将返回的订单列表设置到request域中
		request.setAttribute("orderList", orderList);
		return "f:/jsps/order/list.jsp";
	}

	/**
	 * 加载订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		// 通过oid查询Order
		Order order = orderService.load(oid);
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}

	/**
	 * 确认收获
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得订单编号
		String oid = request.getParameter("oid");
		try {
			orderService.confirm(oid);
			request.setAttribute("msg", "确认收货成功，感谢您的购买，欢迎下次再来！");
			return "f:/jsps/order/msg.jsp";
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/jsps/order/msg.jsp";
		}
	}

	/**
	 * 支付功能
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Properties props = new Properties();
		// 得到配置文件输入流
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		// 加载配置文件
		props.load(inStream);
		// 配置13个参数
		String p0_Cmd = "Buy";// 业务类型
		String p1_MerId = props.getProperty("p1_MerId");// 商户编码
		String p2_Order = request.getParameter("oid");// 订单编号
		String p3_Amt = "0.01";// 交易金额
		String p4_Cur = "CNY";// 币种：人民币
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");// 回馈信息需要的url
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");// 银行类型
		String pr_NeedResponse = "1";// 应答机制

		// 计算hmac
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

		// 连接易宝需要的连接和参数
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);

		// 重定向到易宝
		response.sendRedirect(url.toString());
		return null;
	}

	/**
	 * 回调方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 得到11个参数+hmac
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String hmac = request.getParameter("hmac");

		// 校验访问者是否是易宝
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		props.load(input);
		String keyValue = props.getProperty("keyValue");
		boolean callback = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if (!callback){
			request.setAttribute("msg", "请停止您的违法行为！");
			return "f:/jsps/order/msg.jsp";
		}
		
		// 获取订单状态进行对状态的修改
		orderService.zhiFu(r6_Order);
		request.setAttribute("msg", "订单支付成功，请等待卖家发货！");
		return "f:/jsps/order/msg.jsp";
	}
}
