package com.btl.library.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.btl.library.model.Book;
import com.btl.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/book")
public class BookController {

	private final BookService bookService = new BookService();
	
	@GetMapping("/all") 
	public ResponseEntity<?> getAll() {
		return bookService.getAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) {
		return bookService.getById(id);
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> putBook(@RequestParam("bookData") String obj, @RequestParam(value = "fileImg", required = false) MultipartFile file) throws SQLException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Book book = objectMapper.readValue(obj, Book.class);
		if (bookService.isExistBook(book.getTitle(), book.getAuthor(), -1)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sách đã tồn tại");
		}
		if (file != null) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String uploadDir = "./src/main/resources/static/img/";
			book.setThumb("/img/" + fileName);
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream ips = file.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(ips, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new IOException("Không thể lưu file");
			}
		}
		return bookService.save(book);
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateBook(@PathVariable("id") int id, @RequestParam("bookData") String obj, @RequestParam(value = "fileImg", required = false) MultipartFile file) throws SQLException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Book book = objectMapper.readValue(obj, Book.class);
		if (bookService.isExistBook(book.getTitle(), book.getAuthor(), id)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sách đã tồn tại");
		}
		if (file != null) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String uploadDir = "./src/main/resources/static/img/";
			book.setThumb("/img/" + fileName);
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream ips = file.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(ips, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new IOException("Không thể lưu file");
			}
		}
		return bookService.update(id, book);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePatient(@PathVariable("id") int id) {
		return bookService.delete(id);
	}
}

