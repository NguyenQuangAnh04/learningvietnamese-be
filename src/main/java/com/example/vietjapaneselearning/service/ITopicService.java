package com.example.vietjapaneselearning.service;

import com.example.vietjapaneselearning.dto.TopicDTO;
import com.example.vietjapaneselearning.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITopicService {
    Topic createTopic(TopicDTO topicDTO);
    List<TopicDTO> findAllTopicsByLessonId(Long lessonId);
    Page<TopicDTO> findAllTopics(Long typeGame, Pageable pageable, String name);
}
