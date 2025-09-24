package project.com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.com.model.dao.AdminDao;
import project.com.model.dao.LessonDaoForAdmin;
import project.com.model.entity.Lesson;

@Service
public class AdminLessonService {
	@Autowired
	private LessonDaoForAdmin lessonDaoForAdmin;

	// lession一覧のチェック
	// もしadminId==null 戻り値としてnull
	// findAll内容のコントローラクラスに渡す
	public List<Lesson> selectAllLesson(Long adminId){
		if(adminId == null) {
			return null;
		}else {
			return lessonDaoForAdmin.findByAdmin_AdminId(adminId);
		}
	}
}
