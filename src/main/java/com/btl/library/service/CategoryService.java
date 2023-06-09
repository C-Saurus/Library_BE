package com.btl.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.btl.library.model.Category;

public class CategoryService {
	private String url = "jdbc:mysql://localhost:3306/jdbc_demo";
	private String username = "root";
	private String password = "Matkhaumoi111.";
	
	private static final String GET_ALL = "select * from category";
	public CategoryService(){}
	
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
	
	public ResponseEntity<?> getAll() {
		try (Connection connection = getConnection()){
			List<Category> listCate = new ArrayList<>();
			PreparedStatement ps = connection.prepareStatement(GET_ALL);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				int cateId = result.getInt("category_id");
				String cateName = result.getString("category_name");
				listCate.add(new Category(cateId, cateName));
			}
			connection.close();
			ps.close();
			return ResponseEntity.ok().body(listCate);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã có lỗi xảy ra:" + e.getMessage());
		}
	}
}
