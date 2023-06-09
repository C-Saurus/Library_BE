package com.btl.library.Auth;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btl.library.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService service;
	private UserService userService = new UserService();
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws SQLException {
		if (userService.isExistUser(request.getUsername(), request.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản đã tồn tại");
		}
		try {
			return service.register(request);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Lỗi đăng ký: " + e.getMessage());
		}
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
		try {
			return service.authenticate(request);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Lỗi đăng nhập: " + e.getMessage());
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
	    try {
	    	SecurityContextHolder.clearContext();
	        return ResponseEntity.ok().body("Logout Successfull");
	   } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã đăng xuất: " + e.getMessage());
	    }
	}
}
