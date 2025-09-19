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
public class QuestionDTO {
    private Long gameId;
    private String questionText;
    private String explanation;
    private String image_url;
    private String type;
    private String answerText;
    private List<OptionDTO> options;
}
