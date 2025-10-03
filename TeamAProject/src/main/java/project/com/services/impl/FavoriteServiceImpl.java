package project.com.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.com.model.dao.FavoriteRepository;
import project.com.model.dao.LessonRepository;
import project.com.model.dao.UsersRepository;
import project.com.model.entity.Favorite;
import project.com.model.entity.Lesson;
import project.com.services.FavoriteService;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

	private final FavoriteRepository favoriteRepo;
	private final UsersRepository usersRepo;
	private final LessonRepository lessonRepo;

	@Transactional
	@Override
	public boolean toggle(Long userId, Long lessonId) {
		Optional<Favorite> existing = favoriteRepo.findAll().stream()
				.filter(f -> f.getUser().getUserId().equals(userId) && f.getLesson().getLessonId().equals(lessonId))
				.findFirst();

		if (existing.isPresent()) {
			favoriteRepo.delete(existing.get());
			return false;
		} else {
			Favorite f = Favorite.builder().user(usersRepo.getReferenceById(userId))
					.lesson(lessonRepo.getReferenceById(lessonId)).build();
			favoriteRepo.save(f);
			return true;
		}
	}

	@Override
	public boolean isFavorited(Long userId, Long lessonId) {
		return favoriteRepo.existsByUser_UserIdAndLesson_LessonId(userId, lessonId);
	}

	@Override
	public List<Lesson> getFavoriteLessons(Long userId) {
		return favoriteRepo.findByUser_UserId(userId).stream().map(Favorite::getLesson).toList();
	}

	@Override
	public long countByLesson(Long lessonId) {
		return favoriteRepo.countByLesson_LessonId(lessonId);
	}
}
