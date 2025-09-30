package project.com.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.com.model.dao.TransactionItemRepository;

@Service
@RequiredArgsConstructor
public class PurchaseQueryService {
	private final TransactionItemRepository itemRepo;

    public Set<Long> purchasedLessonIds(Long userId) {
        return itemRepo.findPurchasedLessonIdsByUser(userId);
    }

    public boolean isPurchased(Long userId, Long lessonId) {
        return itemRepo.existsPurchased(userId, lessonId);
    }
}
