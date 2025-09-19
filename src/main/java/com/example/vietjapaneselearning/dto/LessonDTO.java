package com.example.vietjapaneselearning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonDTO {
    private Long id;
    private String title;
    private int status;
    private String describe;
    private String level;
    private String content;
    private LocalDateTime time;
    private List<VocabularyDTO> vocabularies;

}
