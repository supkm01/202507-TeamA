package project.com.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.com.services.TransactionItemService;

@Service
@RequiredArgsConstructor
public class TransactionItemServiceImpl implements TransactionItemService {

    @PersistenceContext
    private final EntityManager em;

    @Override
    @Transactional
    public void insertItem(Long transactionId, Long lessonId) {
        if (transactionId == null) throw new IllegalArgumentException("transactionIdが不正です。");
        if (lessonId == null) throw new IllegalArgumentException("lessonIdが不正です。");

        em.createNativeQuery(
                "INSERT INTO transaction_item (transaction_id, lesson_id) " +
                "VALUES (:txId, :lessonId)"
        )
        .setParameter("txId", transactionId)
        .setParameter("lessonId", lessonId)
        .executeUpdate();
    }
}