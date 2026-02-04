package com.nagarro.digitalMart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nagarro.digitalMart.dto.RegisterRequest;
import com.nagarro.digitalMart.model.User;
import com.nagarro.digitalMart.model.User.Role;
import com.nagarro.digitalMart.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return org.springframework.security.core.userdetails.User.builder()
			.username(user.getUsername())
			.password(user.getPassword())
			.roles(user.getRole().name())
			.build();
	}
	
	/**
	 * Register a new user
	 * @param request Registration request containing username, email, and password
	 * @return The registered user
	 * @throws IllegalArgumentException if username already exists or passwords don't match
	 */
	public User registerUser(RegisterRequest request) throws IllegalArgumentException {
		// Validate input
		if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
			throw new IllegalArgumentException("Username cannot be empty");
		}
		
		if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
		
		if (request.getPassword() == null || request.getPassword().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be empty");
		}
		
		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new IllegalArgumentException("Passwords do not match");
		}
		
		// Check if username already exists
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new IllegalArgumentException("Username already exists");
		}
		
		// Check if email already exists
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already exists");
		}
		
		// Create new user
		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		
		// Save and return
		return userRepository.save(user);
	}
	
	/**
	 * Find user by email
	 * @param email The user's email
	 * @return User if found
	 */
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}

