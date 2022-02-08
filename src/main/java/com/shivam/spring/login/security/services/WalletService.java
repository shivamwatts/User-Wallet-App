package com.shivam.spring.login.security.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shivam.spring.login.exception.WalletAppException;
import com.shivam.spring.login.models.Transaction;
import com.shivam.spring.login.models.User;
import com.shivam.spring.login.payload.response.MessageResponse;
import com.shivam.spring.login.repository.TransactionRepository;
import com.shivam.spring.login.repository.UserRepository;

@Service
public class WalletService {

	private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	UserRepository userRepository;

	public MessageResponse addMoneyToWallet(String username, Long money) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		Long currentBalance = user.getWalletBalance();
		logger.info("add {} money to username {}", money, username);
		user.setWalletBalance(user.getWalletBalance() + money);
		userRepository.save(user);
		Transaction t = new Transaction(user, money + " added to account", currentBalance, user.getWalletBalance());
		transactionRepository.save(t);
		logger.info("add money transaction is successfull");
		return new MessageResponse("Balance Updated");
	}

	public MessageResponse sendMoney(String fromUsername, String toUsername, Long money)
			throws WalletAppException, UsernameNotFoundException {
		User fromUser = userRepository.findByUsername(fromUsername)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + fromUsername));
		if (fromUser.getWalletBalance() < money) {
			throw new WalletAppException("Insufficient balance");
		}
		User toUser = userRepository.findByUsername(toUsername)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + toUsername));
		logger.info("send {} money to username {} from username", money, toUsername, fromUsername);
		Long toUserCurrentBalance = toUser.getWalletBalance();
		Long fromUserCurrentBalance = fromUser.getWalletBalance();
		toUser.setWalletBalance(toUser.getWalletBalance() + money);
		userRepository.save(toUser);
		fromUser.setWalletBalance(fromUser.getWalletBalance() - money);
		userRepository.save(fromUser);
		Transaction fromUserTransaction = new Transaction(fromUser, money + " sent to " + toUsername,
				fromUserCurrentBalance, fromUser.getWalletBalance());
		transactionRepository.save(fromUserTransaction);
		Transaction toUserTransaction = new Transaction(toUser, money + " received from " + fromUser,
				toUserCurrentBalance, toUser.getWalletBalance());
		transactionRepository.save(toUserTransaction);
		logger.info("send money transaction is successfull");
		return new MessageResponse("Transaction Successful");
	}

	public List<Transaction> viewTransactionHistory(Long user_id) {
		return transactionRepository.findAllByUserId(user_id);

	}

}
