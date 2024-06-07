package com.example.springboottabelogkadai.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springboottabelogkadai.entity.Favorite;
import com.example.springboottabelogkadai.entity.Review;
import com.example.springboottabelogkadai.entity.Store;
import com.example.springboottabelogkadai.entity.User;
import com.example.springboottabelogkadai.form.ReservationInputForm;
import com.example.springboottabelogkadai.repository.FavoriteRepository;
import com.example.springboottabelogkadai.repository.ReviewRepository;
import com.example.springboottabelogkadai.repository.StoreRepository;
import com.example.springboottabelogkadai.security.UserDetailsImpl;
import com.example.springboottabelogkadai.service.FavoriteService;
import com.example.springboottabelogkadai.service.ReviewService;

@Controller
@RequestMapping("/stores")
public class StoreController {
	private final StoreRepository storeRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
	
	public StoreController(StoreRepository storeRepository, ReviewRepository reviewRepository,ReviewService reviewService,  FavoriteRepository favoriteRepository, FavoriteService favoriteService) {
		this.storeRepository = storeRepository;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
	}
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			            @RequestParam(name = "area", required = false) String area,
			            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			            Model model)
	{
		Page<Store> storePage;
		
		if (keyword != null && !keyword.isEmpty()) {
			storePage = storeRepository.findByNameLike("%" + keyword + "%", pageable);
		}else if (area != null && !area.isEmpty()) {
		   storePage = storeRepository.findByAddressLike("%" + area + "%", pageable);
	    } else {
	    	storePage = storeRepository.findAll(pageable);
	    }
		
		model.addAttribute("storePage", storePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("area", area);

		return "stores/index";
	}
	
	@GetMapping("/{id}")
	  public String show(@PathVariable(name = "id") Integer id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		  Store store = storeRepository.getReferenceById(id);
		  Favorite favorite = null;
		  boolean userPosted = false;
		  boolean userFavorited = false;
		  
		  if(userDetailsImpl != null) {
			  User user = userDetailsImpl.getUser();
			  userPosted = reviewService.reviewJudge(store, user);
			  userFavorited = favoriteService.favoriteJudge(store, user);
			  
			  if(userFavorited) {
				  favorite = favoriteRepository.findByStoreAndUser(store, user);
			  }
		  }
		  
		  List<Review> reviewList = reviewRepository.findTop6ByStoreOrderByCreatedAtDesc(store);
		  long reviewCount = reviewRepository.countByStore(store);
		  
		  model.addAttribute("store", store);
		  model.addAttribute("reservationInputForm", new ReservationInputForm());
		  model.addAttribute("userPosted", userPosted);
		  model.addAttribute("userFavorited", userFavorited);
		  model.addAttribute("reviewList", reviewList);
		  model.addAttribute("reviewCount", reviewCount);
		  model.addAttribute("favorite", favorite);
		  
		  return "stores/show";
	  }
}
