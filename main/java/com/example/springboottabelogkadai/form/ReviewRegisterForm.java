package com.example.springboottabelogkadai.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRegisterForm {
	private Integer storeId;
	
	private Integer userId;
	
	@NotNull(message = "評価してください(1〜5段階)")
	private Integer score;
	
	@NotBlank(message = "コメントを記入してください。")
	private String content;
}
