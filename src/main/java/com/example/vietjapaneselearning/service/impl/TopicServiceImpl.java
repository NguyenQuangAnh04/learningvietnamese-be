package com.example.vietjapaneselearning.service.impl;

import com.example.vietjapaneselearning.dto.TopicDTO;
import com.example.vietjapaneselearning.model.Game;
import com.example.vietjapaneselearning.model.Topic;
import com.example.vietjapaneselearning.repository.GameRepository;
import com.example.vietjapaneselearning.service.ITopicService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicServiceImpl implements ITopicService {
    private final GameRepository gameRepository;

    public TopicServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Topic createTopic(TopicDTO topicDTO) {
        Optional<Game> game = gameRepository.findById(topicDTO.getGameId());
        return Topic.builder()
                .name(topicDTO.getName())
                .game(game.get())
                .build();
    }
}
