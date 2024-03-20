package com.example.springboottabelogkadai.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboottabelogkadai.entity.Review;
import com.example.springboottabelogkadai.entity.Store;
import com.example.springboottabelogkadai.entity.User;
import com.example.springboottabelogkadai.form.ReviewEditForm;
import com.example.springboottabelogkadai.form.ReviewRegisterForm;
import com.example.springboottabelogkadai.repository.ReviewRepository;
import com.example.springboottabelogkadai.repository.StoreRepository;
import com.example.springboottabelogkadai.repository.UserRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;

	
	public ReviewService(StoreRepository storeRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
	}
	
	@Transactional
	public void create(Store store, User user, ReviewRegisterForm reviewRegisterForm) {
		Review review = new Review();
		
		review.setStore(store);
		review.setUser(user);
		review.setPoint(reviewRegisterForm.getPoint());
		review.setPostContent(reviewRegisterForm.getPostContent());
		
		reviewRepository.save(review);
	}
	
	@Transactional
	public void edit(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		
		review.setPoint(reviewEditForm.getPoint());
		review.setPostContent(reviewEditForm.getPostContent());
		
		reviewRepository.save(review);
	}
	
	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		
		review.setPoint(reviewEditForm.getPoint());
		review.setPostContent(reviewEditForm.getPostContent());
		
		reviewRepository.save(review);
	}
	
	public boolean alreadyReviewed(Store store, User user) {
        return reviewRepository.findByStoreAndUser(store, user) != null;
	}
}
	
