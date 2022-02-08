package com.shivam.spring.login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivam.spring.login.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Optional<Transaction> findById(Long id);
  
  List<Transaction> findAllByUserId(Long user_id);
}
