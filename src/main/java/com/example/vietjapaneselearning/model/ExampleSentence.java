package com.example.vietjapaneselearning.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "example_sentences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleSentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sentence;

    @Column(columnDefinition = "TEXT")
    private String translation;

    @ManyToOne()
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
}
