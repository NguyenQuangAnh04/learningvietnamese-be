package com.example.vietjapaneselearning.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.vietjapaneselearning.dto.LessonDTO;
import com.example.vietjapaneselearning.dto.VocabularyDTO;
import com.example.vietjapaneselearning.model.Lesson;
import com.example.vietjapaneselearning.model.UserLessonProgress;
import com.example.vietjapaneselearning.model.Vocabulary;
import com.example.vietjapaneselearning.repository.GameRepository;
import com.example.vietjapaneselearning.repository.LessonRepository;
import com.example.vietjapaneselearning.repository.UserLessonProgressRepository;
import com.example.vietjapaneselearning.repository.VocabularyRepository;
import com.example.vietjapaneselearning.service.ILessonService;

import jakarta.persistence.EntityNotFoundException;

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
    @Autowired
    private GameRepository gameRepository;

    @Override
    public Page<LessonDTO> findAll(String title, String level, Pageable pageable) {
        Page<Lesson> lessons = lessonRepository.findByTitleAndLevel(title, level.toLowerCase(), pageable);
        return lessons.map(lesson -> {
            UserLessonProgress userLessonProgress = userLessonProgressRepository
                    .findByUserIdAndLessonId(currentUserService.getUserCurrent().getId(), lesson.getId());
            Long gameCount = gameRepository.countGameByLesson(lesson.getId());

            return LessonDTO.builder()
                    .id(lesson.getId())
                    .level(lesson.getLevel())
                    .gameCount(gameCount)
                    .title(lesson.getTitle())
                    .time(lesson.getTime())
                    .content(lesson.getContent())
                    .created(lesson.getCreated())
                    .updated(lesson.getUpdated())
                    .describe(lesson.getContent())
                    .status(userLessonProgressRepository
                            .findByUserIdAndLessonId(currentUserService.getUserCurrent().getId(),
                                    lesson.getId()) != null
                                            ? userLessonProgress.getStatus()
                                            : 0)
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
                .updated(LocalDateTime.now())
                .created(LocalDateTime.now())
                .time(lessonDTO.getTime())
                .level(lessonDTO.getLevel())
                .build();
        lessonRepository.save(lesson);

        List<Vocabulary> vocabularies = lessonDTO.getVocabularies().stream().map(item -> Vocabulary.builder()
                .word(item.getWord())
                .meaning(item.getMeaning())
                .lesson(lesson)
                .build()).toList();
        vocabularyRepository.saveAll(vocabularies);
        return lessonDTO;
    }

    @Override
    public LessonDTO findLessonByTitle(String title) {
        String newTitle = title.replace("-", " ");
        Lesson lesson = lessonRepository.findByTitle(newTitle)
                .orElseThrow(() -> new EntityNotFoundException("Lesson with title " + newTitle + " not found!"));
        List<VocabularyDTO> vocabularies = lesson.getVocabularies().stream().map(item -> {
            return VocabularyDTO.builder()
                    .word(item.getWord())
                    .meaning(item.getMeaning())
                    .pronunciation(item.getPronunciation())
                    .build();
        }).toList();
        return LessonDTO.builder()
                .id(lesson.getId())
                .level(lesson.getLevel())
                .content(lesson.getContent())
                .title(lesson.getTitle())
                .time(lesson.getTime())
                .describe(lesson.getDescription())
                .vocabularies(vocabularies)
                .build();
    }

    @Override
    public LessonDTO updateLesson(LessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(lessonDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id " + lessonDTO.getId() + " not found!"));
        // if(!lessonDTO.getTitle().equals(lesson.getTitle())) {
        // Lesson existingTitleLesson =
        // lessonRepository.findByTitle(lessonDTO.getTitle())
        // .orElseThrow(() -> new EntityNotFoundException("Lesson with title " +
        // lessonDTO.getTitle() + " exists!"));
        // }

        lesson.setTitle(lessonDTO.getTitle());
        lesson.setDescription(lessonDTO.getDescribe());
        lesson.setContent(lessonDTO.getContent());
        lesson.setLevel(lessonDTO.getLevel());
        lesson.setVideo_url(lessonDTO.getVideo_url());
        lesson.setUpdated(LocalDateTime.now());
        lessonRepository.save(lesson);
        return lessonDTO;
    }

    @Override
    public void deleteLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id " + id + " not found!"));
        lessonRepository.delete(lesson);
    }

    private void importVocabularyFromExcel(Long lessonId, String filePath) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id " + lessonId + " not found!"));
        List<Vocabulary> vocabularies = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                String word = row.getCell(0).getStringCellValue();
                String meaning = row.getCell(1).getStringCellValue();
                String pronunciation = row.getCell(2).getStringCellValue();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
