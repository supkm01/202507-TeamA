package project.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import project.com.model.entity.TransactionHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
	boolean existsByTransactionIdAndUser_UserId(Long transactionId, Long userId);

}
