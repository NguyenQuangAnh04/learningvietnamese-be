package com.example.vietjapaneselearning.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultipleChoiceQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionText;
    private String image_url;

    private String explanation;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonIgnore

    private Game game;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Option> options;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}
