package project.com.services.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.com.model.dao.LessonRepository;
import project.com.model.entity.Admin;
import project.com.model.entity.Lesson;

import project.com.services.LessonMenuService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonMenuServiceImpl implements LessonMenuService {
	@Autowired
	private LessonRepository lessonRepository;

	@Override
	public List<Lesson> listAll() {
		return lessonRepository.findAllByOrderByStartDateAscStartTimeAsc();
	}

	// 本日以降に開始するレッスンを開始日・開始時刻の昇順で取得
	@Override
	public List<Lesson> listUpcoming() {
		LocalDate today = LocalDate.now();
		return lessonRepository.findByStartDateGreaterThanEqualOrderByStartDateAscStartTimeAsc(today);
	}

	// 指定された管理者が担当するレッスン一覧を開始日・開始時刻の昇順で取得
	@Override
	public List<Lesson> listByAdmin(Admin admin) {
		return lessonRepository.findByAdminOrderByStartDateAscStartTimeAsc(admin);
	}

	// レッスンIDで1件を検索（存在しない場合はOptional.emptyを返す）
	@Override
	public Optional<Lesson> findById(Long id) {
		return lessonRepository.findById(id);
	}

	// 複数のIDを指定してレッスン一覧を検索
	@Override
	public List<Lesson> findAllById(Iterable<Long> ids) {
		return lessonRepository.findAllById(ids);
	}

}
