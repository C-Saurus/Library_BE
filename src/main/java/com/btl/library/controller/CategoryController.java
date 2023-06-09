package com.btl.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.btl.library.service.CategoryService;

@RestController
@RequestMapping("/cate")
public class CategoryController {
private final CategoryService cateService = new CategoryService();
	@GetMapping("/all") 
	public ResponseEntity<?> getAll() {
		return cateService.getAll();
	}
}
