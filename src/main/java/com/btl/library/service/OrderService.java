package com.btl.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.btl.library.model.Order;


public class OrderService {
	private String url = "jdbc:mysql://localhost:3306/jdbc_demo";
	private String username = "root";
	private String password = "Matkhaumoi111.";
	
	private static final String GET_ORDER_BY_USER_ID = "SELECT o1.order_id, o1.book_id, o1.quantity, o1.price, b1.title from jdbc_demo.order o1\r\n"
			+ "left join jdbc_demo.book b1\r\n"
			+ "on o1.book_id = b1.book_id\r\n"
			+ "where o1.user_id = ?";
	private static final String ADD_NEW = "insert into jdbc_demo.order (user_id, book_id, price, quantity) values (?, ?, ?, ?);";
	private static final String UPDATE_SOLD = "update jdbc_demo.book set sold = sold + ? where book_id = ?;";
	private static final String UPDATE_ORDER = "update jdbc_demo.order set price = ?, quantity = ? where (order_id = ?);";
	private static final String DELETE_ORDER = "delete from jdbc_demo.order where order_id = ?;";
	private static final String FIND_ORDER = "select * from jdbc_demo.order where user_id = ? and book_id = ?";
	public OrderService(){}
	
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return connection;
	}
	public ResponseEntity<?> getOrderByUserId(int userId) {
		try (Connection connection = getConnection()){
			List<Order> listOrder = new ArrayList<>();
			PreparedStatement ps = connection.prepareStatement(GET_ORDER_BY_USER_ID);
			ps.setInt(1, userId);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				int orderId = result.getInt("order_id");
				int bookId = result.getInt("book_id");
				String bookName = result.getString("title");
				int price = result.getInt("price");
				int quantity = result.getInt("quantity");
				listOrder.add(new Order(orderId, bookId, bookName, price, quantity));
			}
			connection.close();
			ps.close();
			return ResponseEntity.ok().body(listOrder);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi:" + e.getMessage());
		}
	}
	
	public ResponseEntity<?> findOrder(int userId, int bookId) {
		List<Order> listOrder = new ArrayList<>();
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(FIND_ORDER);
			ps.setInt(1, userId);
			ps.setInt(2, bookId);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				int orderId = result.getInt("order_id");
				int price = result.getInt("price");
				int quantity = result.getInt("quantity");
				listOrder.add(new Order(orderId, bookId, "", price, quantity));
			}
			connection.close();
			ps.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi:" + e.getMessage());
		}
		return ResponseEntity.ok().body(listOrder);
	}
	
	public ResponseEntity<?> add(Order order) {
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(ADD_NEW);
			ps.setInt(1, order.getUserId());
			ps.setInt(2, order.getBookId());
			ps.setInt(3, order.getPrice());
			ps.setInt(4, order.getQuantity());
			ps.execute();
			ps = connection.prepareStatement(UPDATE_SOLD);
			ps.setInt(1, order.getQuantity());
			ps.setInt(2, order.getBookId());
			ps.execute();
			connection.close();
			ps.close();
			return ResponseEntity.ok().body("Đã thêm vào giỏ hàng <3");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi:" + e.getMessage());
		}
	}
	
	public ResponseEntity<?> update(Order order) {
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(UPDATE_ORDER);
			ps.setInt(1, order.getPrice());
			ps.setInt(2, order.getQuantity());
			ps.setInt(3, order.getOrderId());
			ps.execute();
			connection.close();
			ps.close();
			return ResponseEntity.ok().body("Đã cập nhật giỏ hàng!");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi:" + e.getMessage());
		}
	}
	
	
	
	public ResponseEntity<?> delete(int orderId, int bookId, int quantity) {
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(DELETE_ORDER);
			ps.setInt(1, orderId);
			ps.execute();
			ps = connection.prepareStatement(UPDATE_SOLD);
			ps.setInt(1, 0-quantity);
			ps.setInt(2, bookId);
			ps.execute();
			connection.close();
			ps.close();
			return ResponseEntity.ok().body("Đã xóa thành công!");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã có lỗi xảy ra!");
		}
	}
}
