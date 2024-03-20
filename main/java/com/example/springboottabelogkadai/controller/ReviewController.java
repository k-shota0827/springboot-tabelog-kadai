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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springboottabelogkadai.entity.Review;
import com.example.springboottabelogkadai.entity.Store;
import com.example.springboottabelogkadai.entity.User;
import com.example.springboottabelogkadai.form.ReviewEditForm;
import com.example.springboottabelogkadai.form.ReviewRegisterForm;
import com.example.springboottabelogkadai.repository.ReviewRepository;
import com.example.springboottabelogkadai.repository.StoreRepository;
import com.example.springboottabelogkadai.security.UserDetailsImpl;
import com.example.springboottabelogkadai.service.ReviewService;

@Controller
public class ReviewController {
	private final StoreRepository storeRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	
	public ReviewController(StoreRepository storeRepository, ReviewRepository reviewRepository, ReviewService reviewService) {
		this.storeRepository = storeRepository;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
	}
	
	@GetMapping("/reviews")
	public String review(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
		Store store = new Store();
		User user = userDetailsImpl.getUser();
        Page<Review> reviewPage = reviewRepository.findByUserOrderByCreatedAtDesc(user, pageable);
		
		model.addAttribute("store", store);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		model.addAttribute("reviewPage", reviewPage);
		
		return "reviews/index";
	}
	
	@GetMapping("/restaurants/{id}/review")
	public String review(@PathVariable(name = "id") Integer id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		
		model.addAttribute("store", store);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		
		return "restaurants/review";
	}
	
	@PostMapping("/stores/{storeId}/review/create")
	public String create(@PathVariable(name = "restaurantId") Integer storeId, 
			             @ModelAttribute ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult,  
			             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes, 
			             Model model) {
		Store store = storeRepository.getReferenceById(storeId);
		User user = userDetailsImpl.getUser();
				
		if (bindingResult.hasErrors()) {            
			model.addAttribute("store", store);            
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "stores/show";
        }
		
		reviewService.create(store, user, reviewRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
		
		return "redirect:/stores/{storeId}";
	}
	
	@GetMapping("/stores/{storeId}/review/{reviewId}/edit")
	public String edit(@PathVariable(name = "storeId") Integer storeId, 
			           @PathVariable(name = "reviewId") Integer reviewId, 
			           Model model) {
		Store store = storeRepository.getReferenceById(storeId);
		Review review = reviewRepository.getReferenceById(reviewId);
		
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getUser(), review.getPoint(), review.getPostContent());
		
		model.addAttribute("store", store);
		model.addAttribute("review",review);
		model.addAttribute("reviewEditForm", reviewEditForm);
		
		return "reviews/edit";
	} 
	
	@PostMapping("/stores/{storeId}/review/{reviewId}/update")
	public String update(@PathVariable(name = "storeId") Integer storeId, 
			             @PathVariable(name = "reviewId") Integer reviewId, 
			             @ModelAttribute @Validated ReviewEditForm reviewEditForm, 
			             BindingResult bindingResult, RedirectAttributes redirectAttributes, 
			             Model model) {
		
		Store store = storeRepository.getReferenceById(storeId);
		Review review = reviewRepository.getReferenceById(reviewId);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			model.addAttribute("review",review);
			
			return "reviews/edit";
		}
		
		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		
		return "redirect:/stores/{storeId}";
	}
	
	@PostMapping("/stores/{storeId}/review/{reviewId}/delete")
	public String delete(@PathVariable(name = "reviewId") Integer reviewId, RedirectAttributes redirectAttributes) {
		reviewRepository.deleteById(reviewId);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		
		return "redirect:/stores/{storeId}";
	}
}
