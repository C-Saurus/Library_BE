package com.btl.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.btl.library.service.UserService;

@CrossOrigin
@Controller
@RequestMapping("/user")
public class UserController {
	private final UserService userService = new UserService();
	@GetMapping("/{name}") 
	public ResponseEntity<?> getUser(@RequestParam("name") String username) {
		return userService.getUser(username);
	}
}
