package com.example.vietjapaneselearning.repository;

import com.example.vietjapaneselearning.model.PlayerGame;
import com.example.vietjapaneselearning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerGameRepository extends JpaRepository<PlayerGame, Long> {
    PlayerGame findByUserIdAndGameId(Long UserId, Long gameId);
    @Query("SELECT MAX(pg.attemptNumber) FROM PlayerGame pg WHERE pg.userId = :userId AND pg.gameId = :gameId")
    Integer findMaxAttemptByUserIdAndGameId(@Param("userId") Long userId, @Param("gameId") Long gameId);
    Optional<PlayerGame> findTopByUserIdAndGameIdAndCompletedFalseOrderByAttemptNumberDesc(Long userId, Long gameId);

}
