package project.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.com.model.entity.TransactionItem;

import java.util.List;
import java.util.Set;

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

	@Query("""
			    select distinct ti.lesson.lessonId
			    from TransactionItem ti
			    join ti.transaction t
			    where t.user.userId = :userId
			""")
	Set<Long> findPurchasedLessonIdsByUser(@Param("userId") Long userId);

	@Query("""
			    select case when count(ti) > 0 then true else false end
			    from TransactionItem ti
			    join ti.transaction t
			    where t.user.userId = :userId
			      and ti.lesson.lessonId = :lessonId
			""")
	boolean existsPurchased(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM TransactionItem ti WHERE ti.transaction.id = :txId")
	int deleteByTransactionId(@Param("txId") Long txId);
}
