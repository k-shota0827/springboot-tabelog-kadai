package com.example.springboottabelogkadai.form;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRegisterForm {
	private Integer storeId;
	
	private Integer userId;
	
	private Timestamp createdAt;
	
	private String postContent;
	
	private Integer point;
}
