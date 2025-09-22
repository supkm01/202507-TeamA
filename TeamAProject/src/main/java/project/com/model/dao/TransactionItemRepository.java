package project.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.com.model.entity.TransactionItem;

import java.util.List;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {
	List<TransactionItem> findByTransaction_TransactionId(Long txId);

	List<TransactionItem> findByTransaction_User_UserId(Long userId);

	@Query("""
			    select ti
			      from TransactionItem ti
			      join fetch ti.lesson l
			      join fetch ti.transaction th
			     where th.user.userId = :userId
			     order by th.transactionDate desc, l.startDate, l.startTime
			""")
	List<TransactionItem> findAllForUserWithLessonAndTx(@Param("userId") Long userId);
}
