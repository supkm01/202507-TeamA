package project.com.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.com.model.entity.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	boolean existsByUser_UserIdAndLesson_LessonId(Long userId, Long lessonId);

	long countByLesson_LessonId(Long lessonId);

	List<Favorite> findByUser_UserId(Long userId);
}
