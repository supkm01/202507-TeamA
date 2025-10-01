package project.com.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.com.model.dao.LessonDaoForAdmin;
import project.com.model.entity.Lesson;

@Service
@RequiredArgsConstructor
public class AdminLessonService {
//	@Autowired
//	private LessonDaoForAdmin lessonDaoForAdmin;
	private final LessonDaoForAdmin lessonDaoForAdmin;

	// lession一覧のチェック
	// もしadminId==null 戻り値としてnull
	// findAll内容のコントローラクラスに渡す
	public List<Lesson> selectAllLesson(Long adminId) {
		if (adminId == null) {
			return null;
		} else {
			return lessonDaoForAdmin.findByAdminId(adminId);
		}
	}

	public Lesson saveLesson(Lesson lesson) {
		return lessonDaoForAdmin.save(lesson);
	}

	// lession編集するときのチェック
	// もしlessionId==null 戻り値としてnull
	// そうでない場合、findByLessonIdの情報をコントローラクラスに渡す
	public Lesson LessonEditCheck(Long LessonId) {
		if (LessonId == null) {
			return null;
		} else {
			return lessonDaoForAdmin.findByLessonId(LessonId);
		}
	}

	// 更新処理のチェック
	// もし、productId==nullだったら、更新処理しない
	// false
	// そうでない場合、更新処理する
	public boolean lessonUpdate(Long lessonId, String lessonName, String startDate, String startTime, String finishTime,
			String lessonDetail, String lessonFee, String imageName) {
		if (lessonId == null) {
			return false;
		} else {
			Lesson lesson = lessonDaoForAdmin.findByLessonId(lessonId);
			lesson.setLessonName(lessonName);
			lesson.setStartDate(LocalDate.parse(startDate));
			lesson.setStartTime(LocalTime.parse(startTime));
			lesson.setFinishTime(LocalTime.parse(finishTime));
			lesson.setLessonFee(Integer.parseInt(lessonFee));
			lesson.setImageName(imageName);

			// 更新を保存
			lessonDaoForAdmin.save(lesson);

			return true;
		}

	}

	// 既存の Lesson の画像更新
	public void updateLessonImage(Lesson lesson) throws IOException {
//
//		// DB から既存データを取得
//		Lesson existing = lessonDaoForAdmin.findById(lesson.getLessonId()).orElse(null);
//
//		// 値を上書き
//		existing.setStartDate(lesson.getStartDate());
//		existing.setStartTime(lesson.getStartTime());
//		existing.setFinishTime(lesson.getFinishTime());
//		existing.setLessonName(lesson.getLessonName());
//		existing.setLessonDetail(lesson.getLessonDetail());
//		existing.setLessonFee(lesson.getLessonFee());
//
//		// イメージアアプロード
//		if (imageFile != null && !imageFile.isEmpty()) {
//			String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
//					+ imageFile.getOriginalFilename();
////	            	img保存
//			String uploadDir = "src/main/resources/static/lesson-image/";
//			File dest = new File(uploadDir + fileName);
//			imageFile.transferTo(dest);
//
//			existing.setImageName(fileName);
//		}

		// DB保存
		lessonDaoForAdmin.save(lesson);

	}
	//list表示
	public List<Lesson> findAll(){
		return lessonDaoForAdmin.findAll();
	}
	
	//Lesson削除
	@Transactional
	public boolean deleteLesson(Long lessonId) {
		if(lessonId == null) {
			return false;
		}else {
			lessonDaoForAdmin.deleteByLessonId(lessonId);
			return true;
		}
	}
	
	//検索機能
	public List<Lesson> searchLessonByKeyword(Long adminId, String keyword) {
	    return lessonDaoForAdmin.findByAdminIdAndLessonNameContaining(adminId, keyword);
	}
}
