package com.btl.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.btl.library.model.Comment;
import com.btl.library.service.CommentService;

@RestController
@RequestMapping("/cmt")
public class CommentController {
	private final CommentService cmtService = new CommentService();
	
	@GetMapping("/all")
	public ResponseEntity<?> getCmt(@RequestParam("username") String username, @RequestParam("bookId") int bookId) {
		return cmtService.getByBookId(bookId, username);
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getCmtByUserAndBookId(@RequestParam("username") String username, @RequestParam("bookId") int bookId) {
		return cmtService.getByBookIdAndUsername(bookId, username);
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> addCmt(@RequestBody Comment cmt) {
		return cmtService.add(cmt);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateCmt(@RequestBody Comment cmt) {
		return cmtService.update(cmt);
	}
	
}
