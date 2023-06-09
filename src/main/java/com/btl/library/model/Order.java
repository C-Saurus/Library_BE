package com.btl.library.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Order {
	private int orderId;
	private int bookId;
	private String bookName;
	private int userId;
	@NotNull(message="Price is not null")
	private int price;
	@NotNull(message="Quantity is not null")
	private int quantity;
	public Order(int orderId, int bookId, String bookName, 
			@NotNull(message = "Price is not null") int price,
			@NotNull(message = "Quantity is not null") int quantity) {
		super();
		this.orderId = orderId;
		this.bookId = bookId;
		this.bookName = bookName;
		this.price = price;
		this.quantity = quantity;
	}
	

	
}
