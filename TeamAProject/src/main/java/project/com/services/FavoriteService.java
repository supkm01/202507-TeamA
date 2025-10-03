package project.com.services;

import project.com.model.entity.Lesson;
import java.util.*;

public interface FavoriteService {
    boolean toggle(Long userId, Long lessonId);
    boolean isFavorited(Long userId, Long lessonId);
    List<Lesson> getFavoriteLessons(Long userId);
    long countByLesson(Long lessonId);
}
