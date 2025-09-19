package com.example.vietjapaneselearning.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player_game")
public class PlayerGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startAt;
    private Long userId;       // ID người chơi
    private Long gameId;       // ID game
    private int attemptNumber;
    private int totalScore;    // tổng điểm sau khi hoàn thành game
    private boolean completed; // đánh dấu game đã hoàn thành
    private LocalDateTime completedAt; // thời gian hoàn thành
}
