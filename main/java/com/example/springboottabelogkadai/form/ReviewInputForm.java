package com.example.springboottabelogkadai.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewInputForm {
	@NotNull(message = "評価してください(1〜5段階)")
	private Integer score;
	
	@NotBlank(message = "コメントしてください。")
	private String content;
}
