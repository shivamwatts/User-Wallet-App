package com.shivam.spring.login.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shivam.spring.login.exception.WalletAppException;
import com.shivam.spring.login.payload.request.LoginRequest;
import com.shivam.spring.login.payload.request.SignupRequest;
import com.shivam.spring.login.payload.response.MessageResponse;
import com.shivam.spring.login.security.services.LoginService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	LoginService loginService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		logger.info("sign in API called");
		try {
			String token = loginService.authenticate(loginRequest);
			return ResponseEntity.ok().body(new MessageResponse(token));
		} catch (AuthenticationException exception) {
			logger.error("User not authorized");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new MessageResponse("Error: User not authorized"));
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		logger.info("sign up API called");
		try {
			loginService.register(signUpRequest);
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		} catch (WalletAppException exception) {
			logger.error(exception.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse(exception.getMessage()));
		}
	}

}
