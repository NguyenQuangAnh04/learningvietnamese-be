package com.example.vietjapaneselearning.repository;

import com.example.vietjapaneselearning.model.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
}
