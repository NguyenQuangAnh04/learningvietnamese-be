package com.example.vietjapaneselearning.service;

import com.example.vietjapaneselearning.dto.AnswerDTO;
import com.example.vietjapaneselearning.dto.AnswerResultDTO;
import com.example.vietjapaneselearning.dto.QuestionDTO;
import com.example.vietjapaneselearning.dto.response.StartGameResponse;

public interface IGameService {
    QuestionDTO addQuestion(QuestionDTO dto, Long topicId);
    AnswerResultDTO submitAnswer(AnswerDTO answerDTO);
    StartGameResponse startGame(Long gameId, Long topicId);
}
