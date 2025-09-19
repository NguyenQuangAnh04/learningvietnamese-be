package com.example.vietjapaneselearning.service.impl;

import com.example.vietjapaneselearning.dto.LessonDTO;
import com.example.vietjapaneselearning.model.Lesson;
import com.example.vietjapaneselearning.model.UserLessonProgress;
import com.example.vietjapaneselearning.model.Vocabulary;
import com.example.vietjapaneselearning.repository.LessonRepository;
import com.example.vietjapaneselearning.repository.UserLessonProgressRepository;
import com.example.vietjapaneselearning.repository.VocabularyRepository;
import com.example.vietjapaneselearning.service.ILessonService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements ILessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserLessonProgressRepository userLessonProgressRepository;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private VocabularyRepository vocabularyRepository;
    @Override
    public Page<LessonDTO> findAll(String title, String level, Pageable pageable) {
        Page<Lesson> lessons = lessonRepository.findByTitleAndLevel(title, level, pageable);
        return lessons.map(lesson -> {
            UserLessonProgress userLessonProgress
                     = userLessonProgressRepository.findByUserIdAndLessonId(currentUserService.getUserCurrent().getId(), lesson.getId());
            return LessonDTO.builder()
                    .id(lesson.getId())
                    .level(lesson.getLevel())
                    .time(lesson.getTime())
                    .describe(lesson.getContent())
                    .status(userLessonProgressRepository
                            .findByUserIdAndLessonId(currentUserService.getUserCurrent().getId(), lesson.getId()) != null
                            ? userLessonProgress.getStatus() : 0)
                    .build();
        });
    }

    @Override
    public LessonDTO addLesson(LessonDTO lessonDTO) {
        if (lessonRepository.findByTitle(lessonDTO.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Lesson is already exist on database. Please choice title different!");
        }
        Lesson lesson = Lesson.builder()
                .title(lessonDTO.getTitle())
                .description(lessonDTO.getDescribe())
                .content(lessonDTO.getContent())
                .time(lessonDTO.getTime())
                .level(lessonDTO.getLevel())
                .build();
        lessonRepository.save(lesson);

        List<Vocabulary> vocabularies = lessonDTO.getVocabularies().stream().map(item ->
                Vocabulary.builder()
                        .word(item.getWord())
                        .meaning(item.getMeaning())
                        .lesson(lesson)
                        .build()
        ).toList();
        vocabularyRepository.saveAll(vocabularies);
        return lessonDTO;
    }
}
