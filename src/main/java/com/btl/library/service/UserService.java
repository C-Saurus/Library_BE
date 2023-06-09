package com.btl.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.btl.library.model.Role;
import com.btl.library.model.User;

public class UserService {
	private String url = "jdbc:mysql://localhost:3306/jdbc_demo";
	private String username = "root";
	private String password = "Matkhaumoi111.";
	
	private static final String FIND_BY_NAME = "select password, user_id, role from user where username = ?";
	private static final String ADD_NEW = "insert into user (`email`, `password`, `username`, `role`) VALUES (?, ?, ?, ?);";
	private static final String IS_EXIST_USER = "select * from user where username = ? or email = ?";
	private static final String GET_USER = "select email from user where username = ?";
	public UserService(){}
	
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
	public User findByUsername(String username) {
		User user = null;
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(FIND_BY_NAME);
			ps.setString(1, username);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				user = new User();
				user.setUsername(username);
				user.setPassword(result.getString("password"));
				user.setUserId(result.getInt("user_id"));
                user.setRole(Role.valueOf(result.getString("role")));
			}
			connection.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}
	
	public void putUser(User user) {
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(ADD_NEW);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getUsername());
			ps.setString(4, String.valueOf(user.getRole()));
			ps.execute();
			connection.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			throw new UsernameNotFoundException("User not found");
		}
	}
	
	public boolean isExistUser(String username, String email) {
		ResultSet result = null;
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(IS_EXIST_USER);
			ps.setString(1, username);
			ps.setString(2, email);
			result = ps.executeQuery();
			if (result.next()) return true;
			connection.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
		}
		return false;
	}

	public ResponseEntity<?> getUser(String username) {
		try (Connection connection = getConnection()){
			User user = new User();
			PreparedStatement ps = connection.prepareStatement(GET_USER);
			ps.setString(1, username);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
                user.setUsername(username);
                user.setEmail(result.getString("email"));
			}
			connection.close();
			ps.close();
			return ResponseEntity.ok().body(user);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã có lỗi xảy ra:" + e.getMessage());
		}
	}

}
