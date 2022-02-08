package com.shivam.spring.login.security.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shivam.spring.login.models.User;
import com.shivam.spring.login.payload.request.SignupRequest;
import com.shivam.spring.login.payload.response.MessageResponse;
import com.shivam.spring.login.repository.UserRepository;

@Service
public class ProfileService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
	
	@Autowired
	UserRepository userRepository;
	
	public User viewUserProfile(String username) throws UsernameNotFoundException{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		return user;
	}
	
	public MessageResponse editUserProfile(String username, SignupRequest updatedUser) throws UsernameNotFoundException{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		logger.info("updating username {}", username);
		user.setEmail(updatedUser.getEmail());
		user.setUsername(updatedUser.getUsername());
		user.setPassword(updatedUser.getPassword());
		userRepository.save(user);
		logger.info("successfully updated username {}", username);
		return new MessageResponse("User updated successfully");
	}
}
