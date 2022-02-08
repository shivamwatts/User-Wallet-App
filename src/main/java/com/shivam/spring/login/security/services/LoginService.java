package com.shivam.spring.login.security.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shivam.spring.login.exception.WalletAppException;
import com.shivam.spring.login.models.User;
import com.shivam.spring.login.payload.request.LoginRequest;
import com.shivam.spring.login.payload.request.SignupRequest;
import com.shivam.spring.login.repository.UserRepository;
import com.shivam.spring.login.security.jwt.JwtUtils;

@Service
public class LoginService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	public String authenticate(LoginRequest loginRequest) throws AuthenticationException {
		logger.info("checking authentication for username: {}",loginRequest.getUsername());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String token = jwtUtils.generateToken(userDetails);
		return token;

	}
	
	public void register(SignupRequest signUpRequest) throws WalletAppException {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			logger.error("Error: Username {} is already taken!", signUpRequest.getUsername());
			throw new WalletAppException("Error: Username is already taken!");
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			logger.error("Error: Email {} is already in use!", signUpRequest.getEmail());
			throw new WalletAppException("Error: Email is already in use!");
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), new Long(0));

		userRepository.save(user);
		logger.info("Username {} successfully registered", user.getUsername());
	}

}
