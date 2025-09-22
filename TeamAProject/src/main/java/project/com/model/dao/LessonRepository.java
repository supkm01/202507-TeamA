package project.com.model.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.com.model.entity.Admin;
import project.com.model.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    // search時間順
    List<Lesson> findAllByOrderByStartDateAscStartTimeAsc();

    // search　after today
    List<Lesson> findByStartDateGreaterThanEqualOrderByStartDateAscStartTimeAsc(LocalDate from);

    // search by admin
    List<Lesson> findByAdminOrderByStartDateAscStartTimeAsc(Admin admin);

}
