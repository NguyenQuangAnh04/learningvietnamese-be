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
public class OptionDTO {
    private Long id;
    private String content;
    private boolean isCorrect;

    public OptionDTO(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
