package com.webprogramming.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webprogramming.entity.User;
import com.webprogramming.repository.IUserRepository;
import com.webprogramming.util.PasswordUtil;

@Service
public class UserService {
	
	@Autowired
	private IUserRepository userRepository;
 
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
 
	public Optional<User> findById(int id) {
		return userRepository.findById(id);
	}
 
	public User save(User user) {
		return userRepository.save(user);
	}
 
	/**
	 * Checks credentials and returns the user if they match, matching the
	 * original app's SHA-256 password scheme (so users seeded by the old
	 * project can still log in without a password reset).
	 */
	public Optional<User> authenticate(String email, String rawPassword) {
		return userRepository.findByEmail(email)
				.filter(u -> PasswordUtil.checkPassword(rawPassword, u.getPassword()));
	}
}
