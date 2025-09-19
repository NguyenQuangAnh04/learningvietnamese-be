package com.example.vietjapaneselearning.repository;

import com.example.vietjapaneselearning.model.UserLessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, Long> {
    UserLessonProgress findByUserIdAndLessonId(Long userId, Long lessonId);
}
