package project.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.com.model.dao.LessonDao;
import project.com.model.entity.Lesson;
@Service
public class UserLessonService {
	@Autowired
	LessonDao lessonDao;
	
	//lesson詳細を張り出す
	public Lesson selectByLessonId(Long lessonId) {
		return lessonDao.findByLessonId(lessonId);
	}
	
}
