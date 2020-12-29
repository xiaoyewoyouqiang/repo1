package easymall.service;

import java.util.List;

import easymall.po.Orders;
import easymall.po.OrderItem;

public interface OrderService {

	public void addOrder(String cardIds, Orders myOrder);
	public List<Orders> findOrderByUserId(Integer user_id);
	public List<OrderItem> orderitem(String order_id);
	public void delorder(String id);
	public void payorder(String id);
	
	
}
