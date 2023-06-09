package com.btl.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
	private int id;
	private int bookId;
	private String username;
	private int point;
	private String cmt;
	public Comment(int id, String username, int point, String cmt) {
		super();
		this.id = id;
		this.username = username;
		this.point = point;
		this.cmt = cmt;
	}
	
}
