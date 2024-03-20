package com.example.springboottabelogkadai.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewInputForm {
	@NotBlank(message = "投稿日を選択してください。")
	private String fromCreatedAt;
	
	@NotBlank(message = "コメントを入力してください。")
	private String PostContent;
}
