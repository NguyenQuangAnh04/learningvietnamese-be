package com.example.vietjapaneselearning.repository;

import com.example.vietjapaneselearning.model.Game;
import com.example.vietjapaneselearning.model.Option;
import com.example.vietjapaneselearning.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByGame(Game game);
}
