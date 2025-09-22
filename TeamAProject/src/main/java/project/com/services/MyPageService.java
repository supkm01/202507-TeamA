package project.com.services;

import java.util.List;

import project.com.model.entity.TransactionItem;

public interface MyPageService {
	List<TransactionItem> getPurchasedItems(Long userId);
	void deleteByTransactionId(Long transactionId);
	
}
