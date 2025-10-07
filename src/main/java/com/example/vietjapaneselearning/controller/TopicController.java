package com.example.vietjapaneselearning.controller;

import com.example.vietjapaneselearning.dto.TopicDTO;
import com.example.vietjapaneselearning.model.Topic;
import com.example.vietjapaneselearning.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topic")
public class TopicController {
    @Autowired
    private ITopicService topicService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(@RequestParam(name = "typeGameId", required = false) Long typeGameId,
                                                       @RequestParam(name = "name", required = false) String name,
                                                       @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
        Page<TopicDTO> topicDTOPage = topicService.findAllTopics(typeGameId, pageable, name);

        Map<String, Object> response = new HashMap<>();
        response.put("topics", topicDTOPage.getContent());
        response.put("totalPages", topicDTOPage.getTotalPages() );
        response.put("totalElements", topicDTOPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lessonId}")
    public List<TopicDTO> findAllTopicsByGameId(@PathVariable Long lessonId) {
        return topicService.findAllTopicsByLessonId(lessonId);
    }

    @PostMapping("/add-topic")
    public Topic addTopic(@RequestBody TopicDTO topicDTO) {
        return topicService.createTopic(topicDTO);
    }
}
