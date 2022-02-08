package com.shivam.spring.login.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shivam.spring.login.models.User;
import com.shivam.spring.login.payload.request.SignupRequest;
import com.shivam.spring.login.payload.response.MessageResponse;
import com.shivam.spring.login.payload.response.UserInfoResponse;
import com.shivam.spring.login.security.services.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
	
	@Autowired
	ProfileService profileService;
	
	@GetMapping("/view")
	public ResponseEntity<?> viewProfile(@RequestParam String username) {
		logger.info("view profile API called");
		try {
			User user = profileService.viewUserProfile(username);
			return ResponseEntity.ok().body(
					new UserInfoResponse(user.getId(), user.getUsername(), user.getEmail(), user.getWalletBalance()));
		} catch (UsernameNotFoundException exception) {
			logger.error("Error: Username {} not found!", username);
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username not found!"));
		}
	}

	@PostMapping("/edit")
	public ResponseEntity<?> editProfile(@RequestParam String username,
			@Valid @RequestBody SignupRequest signUpRequest) {
		logger.info("edit profile API called");
		try {
			return ResponseEntity.ok().body(profileService.editUserProfile(username, signUpRequest));
		} catch (UsernameNotFoundException exception) {
			logger.error("Error: Username {} not found!", username);
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username not found!"));
		}
	}
}
