package com.example.springboottabelogkadai.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springboottabelogkadai.entity.Review;
import com.example.springboottabelogkadai.entity.Store;
import com.example.springboottabelogkadai.entity.User;
import com.example.springboottabelogkadai.form.ReviewInputForm;
import com.example.springboottabelogkadai.form.ReviewRegisterForm;
import com.example.springboottabelogkadai.repository.ReviewRepository;
import com.example.springboottabelogkadai.repository.StoreRepository;
import com.example.springboottabelogkadai.security.UserDetailsImpl;

@Controller
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final StoreRepository storeRepository;
	
	public ReviewController(ReviewRepository reviewRepository, StoreRepository storeRepository) {
		this.reviewRepository = reviewRepository;
		this.storeRepository = storeRepository;
	}
	
	@GetMapping("/reviews")
	public String review(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
		User user = userDetailsImpl.getUser();
		Page<Review>reviewPage = reviewRepository.findByUserOrderByCreatedAtDesc(user,  pageable);
		
		model.addAttribute("reviewPage", reviewPage);
		
		return "reviews/confirm";
	}
	
	@GetMapping("/stores/{id}/reviews/input")
	public String input(@PathVariable(name = "id")Integer id,
			            @ModelAttribute @Validated ReviewInputForm reviewInputForm,BindingResult bindingResult,
			            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes,
			            Model model)
	{
		Store store = storeRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
	
	if(bindingResult.hasErrors()) {
		model.addAttribute("store", store);
		model.addAttribute("errorMessage", "予約内容に不備があります。");
		return "stores/show";
	}
	
	redirectAttributes.addFlashAttribute("reviewInputForm", reviewInputForm);
	
	return "redirect:/stores/{id}/reviews/confirm";
	}
	
	@GetMapping("/stores/{id}/reviews/confirm")
	public String confirm(@PathVariable(name ="id") Integer id,
			              @ModelAttribute ReviewInputForm reviewInputForm,
			              @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			              Model model)
	{
		Store store =storeRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		ReviewRegisterForm reviewRegisterForm = new ReviewRegisterForm(store.getId(), user.getId(), reviewInputForm.getScore(), reviewInputForm.getContent());
		
		model.addAttribute("store", store);
		model.addAttribute("reviewRegisterForm", reviewRegisterForm);
		
		return "reviews/confirm";
	}
}
