package com.btl.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.btl.library.model.Order;
import com.btl.library.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	private final OrderService orderService = new OrderService();

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrder(@PathVariable("id") int userId) {
		return orderService.getOrderByUserId(userId);
	}
	
	@GetMapping("/find")
	public ResponseEntity<?> findOrder(@RequestParam("userId") int userId, @RequestParam("bookId") int bookId) {
		return orderService.findOrder(userId, bookId);
	}

	@PostMapping("/save")
	public ResponseEntity<?> add(@RequestBody Order order) {
		return orderService.add(order);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody Order order) {
		return orderService.update(order);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(@RequestParam("orderId") int orderId, @RequestParam("bookId") int bookId, @RequestParam("quantity") int quantity) {
		return orderService.delete(orderId, bookId, quantity);
	}
}
