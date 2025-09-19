package com.example.vietjapaneselearning.service;

import com.example.vietjapaneselearning.dto.TopicDTO;
import com.example.vietjapaneselearning.model.Topic;

public interface ITopicService {
    Topic createTopic(TopicDTO topicDTO);
}
