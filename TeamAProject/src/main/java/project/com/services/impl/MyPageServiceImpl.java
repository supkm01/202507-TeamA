package project.com.services.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.com.model.dao.TransactionItemRepository;
import project.com.model.entity.TransactionItem;
import project.com.services.MyPageService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
	@Autowired
	private TransactionItemRepository transactionItemRepository;

	@PersistenceContext
	private final EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<TransactionItem> getPurchasedItems(Long userId) {
		return transactionItemRepository.findAllForUserWithLessonAndTx(userId);
	}

	@Override
	@Transactional
	public void deleteByTransactionId(Long transactionId) {
		if (transactionId == null)
			throw new IllegalArgumentException("transactionIdが不正です。");
		em.createNativeQuery("DELETE FROM public.transaction_item WHERE transaction_id = :txId")
				.setParameter("txId", transactionId).executeUpdate();
	}
}
