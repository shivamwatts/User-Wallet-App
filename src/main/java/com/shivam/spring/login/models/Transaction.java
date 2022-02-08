package com.shivam.spring.login.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "transaction", uniqueConstraints = { @UniqueConstraint(columnNames = "transaction_id") })
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transaction_id;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotBlank
	@Size(max = 120)
	private String message;

	@NotNull
	private Long currentBalance;

	@NotNull
	private Long updatedBalance;

	public Transaction() {
	}

	public Transaction(User user, String message, Long currentBalance, Long updatedBalance) {
		this.user = user;
		this.message = message;
		this.updatedBalance = updatedBalance;
		this.currentBalance = currentBalance;
	}

	public Long getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Long currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Long getUpdatedBalance() {
		return updatedBalance;
	}

	public void setUpdatedBalance(Long updatedBalance) {
		this.updatedBalance = updatedBalance;
	}

	public Long getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(Long transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
