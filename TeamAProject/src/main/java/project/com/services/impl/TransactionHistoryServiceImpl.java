package project.com.services.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.com.model.dao.LessonRepository;
import project.com.model.dao.TransactionHistoryRepository;
import project.com.model.entity.Lesson;
import project.com.model.entity.TransactionHistory;
import project.com.model.entity.Users;
import project.com.services.TransactionHistoryService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

	@Autowired
	private final TransactionHistoryRepository txRepo;
	@Autowired
	private final LessonRepository lessonRepo;

	@Override
	@Transactional
	public Long record(Users user, Long lessonId) {
		if (user == null)
			throw new IllegalStateException("未ログインです。");
		if (lessonId == null)
			throw new IllegalArgumentException("講座IDが不正です。");

		// tableからデータを取得
		Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new IllegalArgumentException("講座が存在しません。"));

		Integer fee = lesson.getLessonFee();
		if (fee == null)
			fee = 0;

		// amount=lesson.free
		TransactionHistory th = new TransactionHistory();
		th.setUser(user); // @ManyToOne + @JoinColumn("user_id")
		th.setAmount(fee);
		th.setTransactionDate(LocalDateTime.now());

		// saveAndFlush
		th = txRepo.saveAndFlush(th);
		return th.getTransactionId();
	}
	
	 @Override
	    @Transactional
	    public void deleteHeaderForUser(Long transactionId, Long userId) {
	        if (transactionId == null || userId == null)
	            throw new IllegalArgumentException("削除対象またはユーザーが不正です。");

	        boolean owned = txRepo.existsByTransactionIdAndUser_UserId(transactionId, userId);
	        if (!owned) {
	            throw new IllegalStateException("対象の取引が見つからないか、権限がありません。");
	        }
	        txRepo.deleteById(transactionId);
	    }
}
