package com.btl.library.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import com.btl.library.model.Book;

public class BookService {
	private String url = "jdbc:mysql://localhost:3306/jdbc_demo";
	private String username = "root";
	private String password = "Matkhaumoi111.";
	
	private static final String GET_ALL = "select b.book_id, b.title, b.author, b.sold, \r\n"
			+ "c.category_name, b.publication_date, b.numpage, b.thumb FROM jdbc_demo.book b\r\n"
			+ "left join jdbc_demo.category c\r\n"
			+ "on b.category_id = c.category_id;";
	private static final String GET_BY_ID = "select * from book where book_id = ?";
	private static final String SAVE_BOOK = "insert into book (title, author, category_id, \r\n"
			+ "thumb, publication_date, numpage, description, price) \r\n"
			+ "values (?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String UPDATE_BOOK = "update book set title = ?, author = ?, category_id = ?,\r\n"
			+ "thumb = ?, publication_date = ?, numpage = ?, \r\n"
			+ "description = ?, price = ? where (book_id = ?);";
	public static final String DELETE_BOOK = "delete from book where book_id = ?;";
	public static final String IS_EXIST_BOOK = "select * from book where title = ? and author = ? and book_id != ?";
	public BookService(){}
	
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
			List<Book> listBook = new ArrayList<>();
			PreparedStatement ps = connection.prepareStatement(GET_ALL);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				int id = result.getInt("book_id");
				String title = result.getString("title");
				String author = result.getString("author");
				String cate_name = result.getString("category_name");
				String publication_date = String.valueOf(result.getDate("publication_date"));
				int numpage = result.getInt("numpage");
				int sold = result.getInt("sold");
				String thumb = result.getString("thumb");
				listBook.add(new Book(id, title, author, sold, cate_name, publication_date, numpage, thumb));
			}
			connection.close();
			ps.close();
			return ResponseEntity.ok().body(listBook);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã có lỗi xảy ra:" + e.getMessage());
		}
	}
	
	public ResponseEntity<?> getById(int id) {
		try (Connection connection = getConnection()){
			Book book = new Book();
			PreparedStatement ps = connection.prepareStatement(GET_BY_ID);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				book.setBookId(result.getInt("book_id"));
				book.setTitle(result.getString("title"));
				book.setAuthor(result.getString("author"));
				book.setCateId(result.getInt("category_id"));
				book.setDesc(result.getString("description"));
				book.setNumpage(result.getInt("numpage"));
				book.setPubDate(String.valueOf(result.getDate("publication_date")));
				book.setPrice(result.getInt("price"));
				book.setSold(result.getInt("sold"));
				book.setThumb(result.getString("thumb"));

			}
			connection.close();
			ps.close();
			return ResponseEntity.ok().body(book);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã có lỗi xảy ra:" + e.getMessage());
		}
	}
	
	public ResponseEntity<?> save(Book book) {
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(SAVE_BOOK);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setInt(3, book.getCateId());
			ps.setString(4, book.getThumb());
			ps.setDate(5, java.sql.Date.valueOf(book.getPubDate()));
			ps.setInt(6, book.getNumpage());
			ps.setString(7, book.getDesc());
			ps.setInt(8, book.getPrice());
			ps.execute();
			connection.close();
			ps.close();
			return ResponseEntity.ok().body("Đã thêm thành công!");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã có lỗi xảy ra:" + e.getMessage());
		}
	}
	
	public ResponseEntity<?> update(int id, Book book) {
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(UPDATE_BOOK);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setInt(3, book.getCateId());
			ps.setString(4, book.getThumb());
			ps.setDate(5, java.sql.Date.valueOf(book.getPubDate()));
			ps.setInt(6, book.getNumpage());
			ps.setString(7, book.getDesc());
			ps.setInt(8, book.getPrice());
			ps.setInt(9, id);
			ps.execute();
			connection.close();
			ps.close();
			return ResponseEntity.ok().body("Đã sửa thành công!");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã có lỗi xảy ra!");
		}
	}
	
	public ResponseEntity<?> delete(int id) {
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(DELETE_BOOK);
			ps.setInt(1, id);
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
	
	public boolean isExistBook(String title, String author, int id) {
		ResultSet result = null;
		try (Connection connection = getConnection()){
			PreparedStatement ps = connection.prepareStatement(IS_EXIST_BOOK);
			ps.setString(1, title);
			ps.setString(2, author);
			ps.setInt(3, id);
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
	
	public byte[] getByteSrc(String thumb) throws IOException {
		Path filePath = Paths.get(thumb);
		Resource resource = new UrlResource(filePath.toUri());
		if (resource.exists()) {
			byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
			return bytes;
		}
		return null;
	}
}
