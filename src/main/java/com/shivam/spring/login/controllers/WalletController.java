package com.shivam.spring.login.controllers;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shivam.spring.login.exception.WalletAppException;
import com.shivam.spring.login.models.Transaction;
import com.shivam.spring.login.payload.response.MessageResponse;
import com.shivam.spring.login.security.services.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletController {
	
	private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
	
	@Autowired
	WalletService walletService;

	@GetMapping("/add/money")
	@Transactional(rollbackFor = { SQLException.class })
	public ResponseEntity<?> addMoney(@RequestParam String username, @RequestParam Long money) {
		logger.info("add money API called");
		try {
			return ResponseEntity.ok().body(walletService.addMoneyToWallet(username, money));
		} catch (UsernameNotFoundException exception) {
			logger.error("Error: Username {} not found!", username);
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username not found!"));
		}
	}

	@GetMapping("/view/transactions")
	public ResponseEntity<?> viewTransactions(@RequestParam Long userId) {
		logger.info("view transactions API called");
		List<Transaction> transactions = walletService.viewTransactionHistory(userId);
		if (transactions.isEmpty()) {
			logger.info("no transaction found for userid {}", userId);
			return ResponseEntity.ok().body(new MessageResponse("No transactions found"));
		} else {
			return ResponseEntity.ok().body(walletService.viewTransactionHistory(userId));
		}
	}

	@GetMapping("/send/money")
	@Transactional(rollbackFor = { SQLException.class })
	public ResponseEntity<?> sendMoney(@RequestParam String fromUsername, @RequestParam String toUsername,
			@RequestParam Long money) throws UsernameNotFoundException, SQLException {
		logger.info("send money API called");
		try {
			return ResponseEntity.ok().body(walletService.sendMoney(fromUsername, toUsername, money));
		} catch (UsernameNotFoundException exception) {
			logger.error(exception.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse(exception.getMessage()));
		} catch (WalletAppException exception) {
			logger.error(exception.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse(exception.getMessage()));
		}

	}
}
