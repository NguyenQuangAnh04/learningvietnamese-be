package com.example.vietjapaneselearning.repository;

import com.example.vietjapaneselearning.model.PlayerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer, Long> {
    @Query("SELECT COUNT(p) FROM PlayerAnswer p WHERE p.user.id = :userId AND p.gameId = :gameId")
    int countByUserIdAndGameId(@Param("userId") Long userId, @Param("gameId") Long gameId);
    @Query("SELECT COALESCE(SUM(p.point), 0) FROM PlayerAnswer p WHERE p.user.id = :userId AND p.gameId = :gameId")
    int sumPointByUserIdAndGameId(@Param("userId") Long userId, @Param("gameId") Long gameId);
    Optional<PlayerAnswer> findByUserIdAndGameIdAndQuestionId(Long userId, Long gameId, Long questionId);
}
