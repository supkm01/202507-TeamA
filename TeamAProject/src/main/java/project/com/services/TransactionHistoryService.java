package project.com.services;

import project.com.model.entity.Users;

public interface TransactionHistoryService {
	Long record(Users user, Long lessonId);

	void deleteHeaderForUser(Long transactionId, Long userId);
}
