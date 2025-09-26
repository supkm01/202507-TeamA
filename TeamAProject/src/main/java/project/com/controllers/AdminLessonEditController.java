package project.com.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.com.model.entity.Admin;
import project.com.model.entity.Lesson;
import project.com.services.AdminLessonService;

@Controller
@RequestMapping("/admin/lesson")
@RequiredArgsConstructor
public class AdminLessonEditController {
	private final AdminLessonService adminLessonService;

	@Autowired
	private HttpSession session;

	// 講座情報編集フォーム表示
	@GetMapping("/edit/{lessonId}")
	public String showEditForm(@PathVariable Long lessonId, Model model) {
		// sessionから管理者の情報をもらう
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		if (admin == null) {
			return "redirect:/admin/login";
		} else {
			// 編集画面に表示させる情報を変数に格納
			Lesson lesson = adminLessonService.LessonEditCheck(lessonId);
			if (lesson == null) {
				return "redirect:/admin/login";
			} else {
				model.addAttribute("lesson", lesson);
				return "admin_edit_lesson.html";
			}

		}
	}

	@PostMapping("/edit/update")
	public String lessonUpdate(@RequestParam Long lessonId, @RequestParam String lessonName,
			@RequestParam String lessonDetail, @RequestParam String startDate, @RequestParam String startTime,
			@RequestParam String finishTime, @RequestParam String lessonFee, @RequestParam String imageName) {
		// セッションからログインしている管理者情報を取得
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		if (admin == null) {
			// 管理者がログインしていない場合 → ログイン画面へ
			return "redirect:/admin/login";
		} else {
			// DB更新処理
			boolean result = adminLessonService.lessonUpdate(lessonId, lessonName, startDate, startTime, finishTime,
					lessonDetail, lessonFee, imageName);
			if (result) {
				// 成功したら講座一覧へリダイレクト
				return "redirect:/admin/lesson/all";
			} else {
				// 失敗したら再び編集画面へ
				return "redirect:/admin/lesson/edit/" + lessonId;
			}
		}
	}

	// 講座画像変更画面の表示
	@GetMapping("/image/edit/{lessonId}")
	public String showImageEditForm(@PathVariable Long lessonId, Model model) {
		// sessionから管理者の情報をもらう
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		if (admin == null) {
			return "redirect:/admin/login";
		} else {
			// 編集画面に表示させる情報を変数に格納
			Lesson lesson = adminLessonService.LessonEditCheck(lessonId);
			if (lesson == null) {
				return "redirect:/admin/login";
			} else {
				model.addAttribute("lesson", lesson);
				return "admin_edit_lesson_img.html";
			}
		}
	}

	// 講座画像更新処理
	@PostMapping("/image/edit/update")
	public String UpdateLessonImage(@RequestParam("lessonId") Long lessonId,
			@RequestParam("startDate") String startDate, @RequestParam("startTime") String startTime,
			@RequestParam("finishTime") String finishTime, @RequestParam("lessonName") String lessonName,
			@RequestParam("lessonDetail") String lessonDetail, @RequestParam("lessonFee") String lessonFee,
			@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam("registerDate") String registerDate) throws IOException {
//			@RequestParam("adminId") Long adminId
		// セッションからログインしている管理者情報を取得
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		if (admin == null) {
			return "redirect:/admin/login";
		}
		// ファイル名を生成（保存時の重複回避）
		String fileName = null;
		if (imageFile != null && !imageFile.isEmpty()) {
			fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
					+ imageFile.getOriginalFilename();
		}
		try {
			Files.copy(imageFile.getInputStream(), Path.of("src/main/resources/static/lesson-image/" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Lesson エンティティを作成して service に渡す
		Lesson lesson = new Lesson();
		lesson.setLessonId(lessonId);
		lesson.setStartDate(LocalDate.parse(startDate));
		lesson.setStartTime(LocalTime.parse(startTime));
		lesson.setFinishTime(LocalTime.parse(finishTime));
		lesson.setLessonName(lessonName);
		lesson.setLessonDetail(lessonDetail);
		lesson.setLessonFee(Integer.parseInt(lessonFee));
		lesson.setRegisterDate(LocalDateTime.parse(registerDate));
		lesson.setAdminId(admin.getAdminId());
		
		if (fileName != null) {
			lesson.setImageName(fileName);
		}

		// 更新処理
		adminLessonService.updateLessonImage(lesson);

		return "redirect:/admin/lesson/all"; // 成功したら一覧へ

	}

}
