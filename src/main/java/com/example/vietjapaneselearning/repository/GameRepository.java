package com.example.vietjapaneselearning.repository;

import com.example.vietjapaneselearning.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
