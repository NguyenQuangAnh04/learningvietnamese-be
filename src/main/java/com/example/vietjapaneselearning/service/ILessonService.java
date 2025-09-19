package com.example.vietjapaneselearning.service;

import com.example.vietjapaneselearning.dto.LessonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILessonService {
    Page<LessonDTO> findAll(String title, String level,  Pageable pageable);
    LessonDTO addLesson(LessonDTO lessonDTO);

}
