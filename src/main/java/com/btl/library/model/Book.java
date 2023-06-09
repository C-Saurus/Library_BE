package com.btl.library.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Book {
	private int bookId;
	@NotBlank(message="Title is not blank")
	private String title;
	@NotBlank(message="Author is not blank")
	private String author;
	private int sold;
	@NotNull(message="Category is not null")
	private int cateId;
	private String cateName;
	private String thumb;
	@NotBlank(message="Publication Date is not blank")
	private String pubDate;
	@NotNull(message="Numpage is not null")
	private int numpage;
	private String desc;
	@NotNull(message="Price is not null")
	private int price;
	public Book(int bookId, @NotBlank(message = "Title is not blank") String title,
			@NotBlank(message = "Author is not blank") String author, int sold, String cateName,
			@NotBlank(message = "Publication Date is not blank") String pubDate,
			@NotNull(message = "Numpage is not null") int numpage, String thumb) {
		super();
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.sold = sold;
		this.cateName = cateName;
		this.pubDate = pubDate;
		this.numpage = numpage;
		this.thumb = thumb;
	}
}
