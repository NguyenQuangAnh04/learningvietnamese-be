package com.example.vietjapaneselearning.repository;

import com.example.vietjapaneselearning.model.MultipleChoiceQuestion;
import com.example.vietjapaneselearning.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MultipleChoiceGameQuestionRepository extends JpaRepository<MultipleChoiceQuestion, Long> {
    long countByGameId(Long gameId);
    List<MultipleChoiceQuestion> findByTopic(Topic topic);
}
