package com.example.vietjapaneselearning.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vietjapaneselearning.model.Lesson;
import com.example.vietjapaneselearning.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
        List<Topic> findByLesson(Lesson lesson);

        @Query("SELECT t FROM Topic t " +
                        "WHERE (:typeGame is null OR t.game.id = :typeGame) " +
                        "AND (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')))")
        Page<Topic> findByTypeGameAndName(@Param("typeGame") Long typeGame,
                        @Param("name") String name,
                        Pageable pageable);

}
