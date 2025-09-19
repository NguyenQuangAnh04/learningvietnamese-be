package com.example.vietjapaneselearning.repository;

import com.example.vietjapaneselearning.model.FillBlankQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FillBlankQuestionRepository extends JpaRepository<FillBlankQuestion, Long> {
    long countByGameId(Long gameId);
}
