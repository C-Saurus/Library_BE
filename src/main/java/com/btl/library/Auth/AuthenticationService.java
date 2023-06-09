package com.btl.library.Auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.btl.library.jwt.JwtService;
import com.btl.library.model.Role;
import com.btl.library.model.User;
import com.btl.library.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserService userService = new UserService();
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	public ResponseEntity<?> register(RegisterRequest request) {
		if (request.getEmail().equals(null) || request.getPassword().equals(null) || request.getUsername().equals(null)
				|| request.getEmail().isEmpty() || request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
		}
		var user = User.builder().email(request.getEmail()).username(request.getUsername())
				.password(encoder.encode(request.getPassword())).role(Role.USER).build();
		userService.putUser(user);
		return ResponseEntity.ok().body("Đăng kí thành công!");
	}

	public ResponseEntity<?> authenticate(AuthenticationRequest request) {
		if (request.getPassword() == null || request.getUsername() == null || request.getUsername().isEmpty()
				|| request.getPassword().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
		}
		var user = userService.findByUsername(request.getUsername());
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
		}
		if (!encoder.matches(request.getPassword(),user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai mật khẩu đăng nhập");
		}
		try {
			authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (AuthenticationException e) {
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
		}
		var jwtToken = jwtService.generateToken(user);
		var response = AuthenticationResponse.builder().token(jwtToken).role(String.valueOf(user.getRole())).id(user.getUserId()).username(request.getUsername()).build();
		return ResponseEntity.ok().body(response);
	}

}
