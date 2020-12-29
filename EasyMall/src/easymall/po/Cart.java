package easymall.po;

public class Cart {
	private Integer cartID;
	private Integer user_id;
	private String username;
	private String pid;
	private Integer num;
	public Cart(){}
	public Cart( Integer user_id, String pid, Integer num) {
		this.user_id = user_id;
		this.pid = pid;
		this.num = num;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getCartID() {
		return cartID;
	}
	public void setCartID(Integer cartID) {
		this.cartID = cartID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
}
