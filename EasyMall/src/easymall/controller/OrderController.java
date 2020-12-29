package easymall.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import easymall.po.Orders;
import easymall.po.OrderItem;
import easymall.po.Products;
import easymall.po.User;
import easymall.pojo.MyCart;
import easymall.pojo.OrderInfo;
import easymall.service.CartService;
import easymall.service.OrderService;
import easymall.service.ProductsService;

@Controller("orderController")
@RequestMapping("/order")
public class OrderController extends BaseController{
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductsService productsService;
	
	@RequestMapping("/payorder")
	public String payorder(String id,Model model) {
		orderService.payorder(id);
		return "redirect:/order/showorder";
	}
	@RequestMapping("/delorder")
	public String delorder(String id,Model model) {
		orderService.delorder(id);
		return "redirect:/order/showorder";
	}
	@RequestMapping("addOrder")
	public String addOrder(String receiverinfo,String cartIds,HttpSession session) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(new Date());
		Timestamp timeStamp = Timestamp.valueOf(time);
		User user = (User) session.getAttribute("user");
		String orderId = UUID.randomUUID().toString();
		Orders myOrder = new Orders(orderId,null,receiverinfo,0,timeStamp,user.getId());
		orderService.addOrder(cartIds,myOrder);
		return "forward:/order/showorder";
	}
	
	@RequestMapping("/showorder")
	public String showorder(HttpSession session,Model model) {
//		1. 获取当前登录用户
		User user = (User) session.getAttribute("user");
//		2. 根据用户id查询当前用户所有订单信息，查询orders表
		List<OrderInfo> orderInfoList = findOrderInfoByUserId(user.getId());
//		3. 将该用户的所有订单信息的list集合存入request域中，转发到order_list.jsp中显示
		model.addAttribute("orderInfos",orderInfoList);
		return "order_list";
		
	}
	
	public List<OrderInfo> findOrderInfoByUserId(Integer userId){
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
//		1. 根据用户id查询当前用户所有订单信息，查询orders表
		List<Orders> orderList = orderService.findOrderByUserId(userId);
//		2. 遍历每个订单，通过订单id查询当前订单中包含的所有订单信息
		for(Orders order:orderList) {
			List<OrderItem> orderitems = orderService.orderitem(order.getId());
			Map<Products,Integer> map = new HashMap<Products,Integer>();
			for(OrderItem orderitem:orderitems) {
				Products product = productsService.oneProduct(orderitem.getProduct_id());
				map.put(product, orderitem.getBuynum());
			}
			OrderInfo orderinfo = new OrderInfo();
			orderinfo.setOrder(order);
			orderinfo.setMap(map);
			orderInfoList.add(orderinfo);
			
		}
		return orderInfoList;
		
	}
	
	@RequestMapping("order_add")
	public String order_add(String cartIds,Model model) {
		//1.将购物车中所有选中商品的cartID组合起来的字符串拆分为数组
		String[] arrCartIds = cartIds.split(",");
		
		List<MyCart> carts = new ArrayList<MyCart>();
		//2.遍历数组，根据cartID编号查找购物车，添加到carts中
		for(String cid : arrCartIds) {
			Integer cartID = Integer.parseInt(cid);
			MyCart cart = cartService.findByCartID(cartID);
			carts.add(cart);
		}
		model.addAttribute("carts",carts);
		model.addAttribute("cartIds",cartIds);
		return ("order_add");
	}
	
	
	
}
