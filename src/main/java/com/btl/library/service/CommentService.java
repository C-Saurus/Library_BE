package com.btl.library.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.btl.library.model.Comment;

public class CommentService {
	private String url = "jdbc:mysql://localhost:3306/jdbc_demo";
	private String username = "root";
	private String password = "Matkhaumoi111.";

	private static final String GET_BY_BOOK_ID = "select * from comment where book_id = ? and username != ?";
	private static final String GET_BY_BOOK_ID_AND_USERNAME = "select id, point, cmt from comment where (book_id = ? and username = ?)";
	private static final String ADD_NEW = "insert into comment (book_id, username, point, cmt) values (?, ?, ?, ?);";
	private static final String UPDATE_CMT = "update jdbc_demo.comment set point = ?, cmt = ? where id = ?;";

	public CommentService() {
	}

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

	public ResponseEntity<?> getByBookIdAndUsername(int bookId, String username) {
		try (Connection connection = getConnection()) {
			Comment cmt = new Comment();
			PreparedStatement ps = connection.prepareStatement(GET_BY_BOOK_ID_AND_USERNAME);
			ps.setInt(1, bookId);
			ps.setString(2, username);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				cmt.setId(result.getInt("id"));
				cmt.setBookId(bookId);
				cmt.setUsername(username);
				cmt.setPoint(result.getInt("point"));
				cmt.setCmt(result.getString("cmt"));
			}
			connection.close();
			ps.close();
			return ResponseEntity.ok().body(cmt);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi:" + e.getMessage());
		}
	}
	public ResponseEntity<?> getByBookId(int bookId, String username) {
		try (Connection connection = getConnection()) {
			List<Comment> listCmt = new ArrayList<>();
			PreparedStatement ps = connection.prepareStatement(GET_BY_BOOK_ID);
			ps.setInt(1, bookId);
			ps.setString(2, username);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				int id = result.getInt("id");
				String username1 = result.getString("username");
				int point = result.getInt("point");
				String cmt = result.getString("cmt");
				listCmt.add(new Comment(id, username1, point, cmt));
			}
			connection.close();
			ps.close();
			return ResponseEntity.ok().body(listCmt);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi:" + e.getMessage());
		}
	}

	public ResponseEntity<?> add(Comment cm) {
		try (Connection connection = getConnection()) {
			PreparedStatement ps = connection.prepareStatement(ADD_NEW);
			ps.setInt(1, cm.getBookId());
			ps.setString(2, cm.getUsername());
			ps.setInt(3, cm.getPoint());
			ps.setString(4, cm.getCmt());
			ps.execute();
			connection.close();
			ps.close();
			return ResponseEntity.ok().body("Cảm ơn bạn đã đánh giá <3");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi:" + e.getMessage());
		}
	}
	
	public ResponseEntity<?> update(Comment cm) {
		try (Connection connection = getConnection()) {
			PreparedStatement ps = connection.prepareStatement(UPDATE_CMT);
			ps.setInt(1, cm.getPoint());
			ps.setString(2, cm.getCmt());
			ps.setInt(3, cm.getId());
			ps.execute();
			connection.close();
			ps.close();
			return ResponseEntity.ok().body("Đã cập nhật đánh giá <3");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi:" + e.getMessage());
		}
	}
	
}
