package com.example.vietjapaneselearning.service.impl;

import com.example.vietjapaneselearning.dto.TopicDTO;
import com.example.vietjapaneselearning.model.Game;
import com.example.vietjapaneselearning.model.Lesson;
import com.example.vietjapaneselearning.model.PlayerGame;
import com.example.vietjapaneselearning.model.Topic;
import com.example.vietjapaneselearning.repository.GameRepository;
import com.example.vietjapaneselearning.repository.LessonRepository;
import com.example.vietjapaneselearning.repository.PlayerGameRepository;
import com.example.vietjapaneselearning.repository.TopicRepository;
import com.example.vietjapaneselearning.service.ITopicService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TopicServiceImpl implements ITopicService {
    private final GameRepository gameRepository;
    private final TopicRepository topicRepository;
    private final PlayerGameRepository playerGameRepository;
    private final CurrentUserService currentUserService;
    private final LessonRepository lessonRepository;

    @Override
    public Topic createTopic(TopicDTO topicDTO) {
        Optional<Game> game = gameRepository.findById(topicDTO.getGameId());
        return Topic.builder()
                .name(topicDTO.getName())
                .build();
    }

    @Override
    public List<TopicDTO> findAllTopicsByLessonId(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Not found lessonId"));
        List<Topic> topics = topicRepository.findByLesson(lesson);
        if (topics.isEmpty()) {
            return Collections.emptyList();
        }
        List<TopicDTO> dtos = topics.stream().map(item ->
                {
                    Optional<PlayerGame> lastPlay = playerGameRepository
                            .findTopByLessonIdAndUserIdAndGameIdOrderByStartAtDesc(item.getId(), currentUserService.getUserCurrent().getId(), lesson.getId());
                    boolean comleted = lastPlay.map(PlayerGame::isCompleted).orElse(false);
                    Long questionId = lastPlay
                            .map(PlayerGame::getQuestionId)
                            .filter(qId -> qId != null && qId > 0)
                            .orElse(null);
                    return TopicDTO.builder()
                            .name(item.getName())
                            .score((long) lastPlay.get().getTotalScore())
                            .completed(comleted)
                            .id(item.getId())
                            .questionId(questionId)
                            .description(item.getDescription())
                            .build();
                }
        ).toList();
        return dtos;
    }

    @Override
    public Page<TopicDTO> findAllTopics(Long typeGame, Pageable pageable, String name) {
        log.info("GameId: {}", typeGame);
        Page<Topic> topics = topicRepository.findByTypeGameAndName(typeGame, name, pageable);
        return topics.map(item -> {
            return TopicDTO.builder()
                    .name(item.getName())
                    .id(item.getId())
                    .createdAt(item.getCreateAt())
                    .description(item.getDescription())
                    .build();
        });
    }
}
