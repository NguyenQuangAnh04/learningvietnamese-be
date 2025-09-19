package com.example.vietjapaneselearning.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long questionId; // có thể là MC hoặc FillBlank
    private Long gameId;
    private String userAnswer; // "A", "B", "Apple", ...
    private int point;
    private boolean isCorrect;

    private LocalDateTime answeredAt;
}
