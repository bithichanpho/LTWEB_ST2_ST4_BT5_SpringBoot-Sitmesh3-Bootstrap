package com.webprogramming.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	public void deleteById(int id) {
		userRepository.deleteById(id);
	}


	public Page<User> search(String keyword, int zeroBasedPage, int pageSize) {
		Pageable pageable = PageRequest.of(Math.max(zeroBasedPage, 0), pageSize,
				Sort.by(Sort.Direction.DESC, "userId"));
		String kw = keyword == null ? "" : keyword.trim();
		return userRepository.findByFullnameContainingIgnoreCaseOrEmailContainingIgnoreCase(kw, kw, pageable);
	}

	public Optional<User> authenticate(String email, String rawPassword) {
		return userRepository.findByEmail(email)
				.filter(u -> u.getStatus() == 1)
				.filter(u -> PasswordUtil.checkPassword(rawPassword, u.getPassword()));
	}
}
