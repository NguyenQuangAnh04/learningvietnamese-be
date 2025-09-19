package com.example.vietjapaneselearning.controller;

import com.example.vietjapaneselearning.dto.LessonDTO;
import com.example.vietjapaneselearning.service.ILessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {
    @Autowired
    private ILessonService lessonService;
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(@RequestParam(name = "title", required = false) String title,
                                                        @RequestParam(name = "level", required = false) String level,
                                                        @RequestParam(name = "page", required = false, defaultValue = "0") int page){
        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("id"));
        Map<String, Object> response = new HashMap<>();
        Page<LessonDTO> lessonDTOPage = lessonService.findAll(title, level, pageRequest);
        response.put("lesson", lessonDTOPage.getContent());
        response.put("totalPage", lessonDTOPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add_lesson")
    public ResponseEntity<LessonDTO> addLesson(@RequestBody LessonDTO lessonDTO){
        return ResponseEntity.ok(lessonService.addLesson(lessonDTO));
    }
}
