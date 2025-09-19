package com.example.vietjapaneselearning.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FillBlankQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    private String correctAnswer;

    private String explanation;

    private Integer orderNum;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonIgnore

    private Game game;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}
