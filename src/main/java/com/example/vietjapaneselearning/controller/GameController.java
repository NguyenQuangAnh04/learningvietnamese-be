package com.example.vietjapaneselearning.controller;

import com.example.vietjapaneselearning.dto.AnswerDTO;
import com.example.vietjapaneselearning.dto.AnswerResultDTO;
import com.example.vietjapaneselearning.dto.QuestionDTO;
import com.example.vietjapaneselearning.dto.response.StartGameResponse;
import com.example.vietjapaneselearning.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private IGameService gameService;

    @PostMapping("/add_question/{topicId}")
    public ResponseEntity<QuestionDTO> addQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable(name = "topicId") Long topicId) {
        return ResponseEntity.ok(gameService.addQuestion(questionDTO, topicId));
    }

    @PostMapping("/submit_answer")
    public ResponseEntity<AnswerResultDTO> submitAnswer(@RequestBody AnswerDTO dto) {
        AnswerResultDTO result = gameService.submitAnswer(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{gameId}/topics/{topicId}/start")
    public ResponseEntity<StartGameResponse> startGame(
            @PathVariable Long gameId,
            @PathVariable Long topicId
    ) {
        StartGameResponse response = gameService.startGame(gameId, topicId);
        return ResponseEntity.ok(response);
    }
}
