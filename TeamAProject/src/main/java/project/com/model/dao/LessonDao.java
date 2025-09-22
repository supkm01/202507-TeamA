package project.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.com.model.entity.Lesson;

@Repository
public interface LessonDao extends JpaRepository<Lesson, Long> {
	
	//LessonIDを使用してDBに検索をかける
	Lesson findByLessonId(Long lessonId);
	
	
	
}
