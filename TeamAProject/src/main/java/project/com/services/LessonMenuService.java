package project.com.services;

import project.com.model.entity.Admin;
import project.com.model.entity.Lesson;
import java.util.List;
import java.util.Optional;

public interface LessonMenuService {
	//search all
    List<Lesson> listAll();
    //search aftertoday
    List<Lesson> listUpcoming();
    //search byAdmin
    List<Lesson> listByAdmin(Admin admin);
    
    List<Lesson> findAllById(Iterable<Long> ids);
    //detail
    Optional<Lesson> findById(Long id);
}
