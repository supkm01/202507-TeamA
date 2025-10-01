package project.com.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.com.model.entity.Lesson;

public interface LessonDaoForAdmin extends JpaRepository<Lesson, Long> {
	// 保存処理と更新処理 insert update
	Lesson save(Lesson lesson);

	// SELECT * FROM Lesson
	// 用途：lesson一覧を表示させるときに使用。
	List<Lesson> findAll();

	// SELECT * FROM Lesson WHERE admin_id = ?
	// 用途：AdminごとのLessonを表示させる
	List<Lesson> findByAdminId(Long adminId);

	// SELECT * FROM lesson WHERE lesson_name = ?
	// 用途：Lessonの登録チェックに使用。
	Lesson findByLessonName(String lessonName);

	// SELECT * FROM lesson WHERE blog_id = ?
	// 用途：編集する時に使用。
	Lesson findByLessonId(Long LessonId);

	// DLETE FROM lesson WHERE blog_id = ?
	// 用途：削除 ！！！@Transactional が宣言必要です
	void deleteByLessonId(Long lessonId);
	
	// SELECT
	List<Lesson> findByAdminIdAndLessonNameContaining(Long adminId, String lessonName);
}
