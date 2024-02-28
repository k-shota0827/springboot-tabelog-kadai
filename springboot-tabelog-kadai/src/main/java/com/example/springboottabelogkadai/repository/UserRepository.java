package com.example.springboottabelogkadai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboottabelogkadai.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmail(String email);
}
