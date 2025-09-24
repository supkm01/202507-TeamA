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

	// 取引履歴を新規登録（ユーザーが講座を購入した際に呼ばれる）
	@Override
	@Transactional
	public Long record(Users user, Long lessonId) {
		if (user == null)
			// 未ログイン時は処理不可
			throw new IllegalStateException("未ログインです。");
		if (lessonId == null)
			// 引数の講座IDが不正
			throw new IllegalArgumentException("講座IDが不正です。");

		// 講座IDからレッスン情報を取得（存在しない場合は例外）
		Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new IllegalArgumentException("講座が存在しません。"));

		// 講座料金を取得（nullの場合は0円とする）
		Integer fee = lesson.getLessonFee();
		if (fee == null)
			fee = 0;

		// 新しい取引履歴エンティティを作成
		TransactionHistory th = new TransactionHistory();
		th.setUser(user); // @ManyToOne + @JoinColumn("user_id")
		th.setAmount(fee);
		th.setTransactionDate(LocalDateTime.now());

		// DBへ即時保存
		th = txRepo.saveAndFlush(th);
		return th.getTransactionId();
	}

	// 指定ユーザーに紐づく取引履歴ヘッダを削除
	@Override
	@Transactional
	public void deleteHeaderForUser(Long transactionId, Long userId) {
		if (transactionId == null || userId == null)
			// 引数チェック
			throw new IllegalArgumentException("削除対象またはユーザーが不正です。");
		// 該当ユーザーが対象取引を所有しているか確認
		boolean owned = txRepo.existsByTransactionIdAndUser_UserId(transactionId, userId);
		if (!owned) {
			// 見つからない場合、または他人の取引なら削除不可
			throw new IllegalStateException("対象の取引が見つからないか、権限がありません。");
		}
		// 取引ヘッダを削除
		txRepo.deleteById(transactionId);
	}
}
