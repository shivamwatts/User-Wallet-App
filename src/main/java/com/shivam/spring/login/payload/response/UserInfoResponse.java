package com.shivam.spring.login.payload.response;

public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private Long walletBalance;

	public UserInfoResponse(Long id, String username, String email, Long walletBalance) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.setWalletBalance(walletBalance);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(Long walletBalance) {
		this.walletBalance = walletBalance;
	}

}
