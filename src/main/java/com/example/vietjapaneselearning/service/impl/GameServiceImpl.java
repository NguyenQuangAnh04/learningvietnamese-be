package com.example.vietjapaneselearning.service.impl;

import com.example.vietjapaneselearning.dto.AnswerDTO;
import com.example.vietjapaneselearning.dto.AnswerResultDTO;
import com.example.vietjapaneselearning.dto.OptionDTO;
import com.example.vietjapaneselearning.dto.QuestionDTO;
import com.example.vietjapaneselearning.dto.response.StartGameResponse;
import com.example.vietjapaneselearning.model.*;
import com.example.vietjapaneselearning.repository.*;
import com.example.vietjapaneselearning.service.IGameService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class GameServiceImpl implements IGameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private MultipleChoiceGameQuestionRepository multipleChoiceGameQuestionRepository;
    @Autowired
    private FillBlankQuestionRepository fillBlankQuestionRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private PlayerAnswerRepository playerAnswerRepository;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private PlayerGameRepository playerGameRepository;
    @Autowired
    private TopicRepository topicRepository;

    @Override
    public QuestionDTO addQuestion(QuestionDTO dto, Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Not fount topic with id: " + topicId));
        Game game = gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> new EntityNotFoundException("Not found with game id " + dto.getGameId()));
        if (dto.getType().equals("MC")) {
            MultipleChoiceQuestion mc = MultipleChoiceQuestion.builder()
                    .questionText(dto.getQuestionText())
                    .explanation(dto.getExplanation())
                    .image_url(dto.getImage_url())
                    .topic(topic)
                    .game(game)
                    .build();
            mc = multipleChoiceGameQuestionRepository.save(mc);
            if (!dto.getOptions().isEmpty()) {
                MultipleChoiceQuestion multipleChoiceQuestion = mc;
                List<Option> options = dto.getOptions().stream()
                        .map(opt -> Option.builder()
                                .question(multipleChoiceQuestion)
                                .text(opt.getContent())
                                .isCorrect(opt.isCorrect())
                                .build())
                        .toList();
                optionRepository.saveAll(options);
            }
        } else if (dto.getType().equals("FB")) {
            FillBlankQuestion fb = FillBlankQuestion.builder()
                    .game(game)
                    .correctAnswer(dto.getAnswerText())
                    .questionText(dto.getQuestionText())
                    .explanation(dto.getExplanation())
                    .build();
            fillBlankQuestionRepository.save(fb);
        }
        return dto;
    }

    @Override
    public AnswerResultDTO submitAnswer(AnswerDTO answerDTO) {
        int point = 10;
        AnswerResultDTO result;
        if (answerDTO.getGameId() == 1) { // Multiple Choice
            MultipleChoiceQuestion mc = multipleChoiceGameQuestionRepository.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Not found id question " + answerDTO.getGameId()));
            List<Option> options = optionRepository.findAllByQuestionId(mc.getId());

            boolean correct = options.stream()
                    .anyMatch(opt -> opt.isCorrect() && opt.getId().equals(answerDTO.getOptionId()));

            PlayerAnswer playerAnswer = PlayerAnswer.builder()
                    .userAnswer(String.valueOf(answerDTO.getOptionId()))
                    .questionId(answerDTO.getQuestionId())
                    .gameId(answerDTO.getGameId())
                    .user(currentUserService.getUserCurrent())
                    .isCorrect(correct)
                    .answeredAt(LocalDateTime.now())
                    .point(correct ? point : 0)
                    .build();
            playerAnswerRepository.save(playerAnswer);

            result = AnswerResultDTO.builder()
                    .correct(correct)
                    .explanation(mc.getExplanation())
                    .options(options)
                    .build();

        } else if (answerDTO.getGameId() == 2) { // Fill in Blank
            FillBlankQuestion fb = fillBlankQuestionRepository.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Not found id question " + answerDTO.getGameId()));

            String[] correctAnswers = fb.getCorrectAnswer().split(";");
            boolean correct = Arrays.stream(correctAnswers)
                    .anyMatch(ans -> ans.trim().equalsIgnoreCase(answerDTO.getAnswer().trim()));

            PlayerAnswer playerAnswer = PlayerAnswer.builder()
                    .userAnswer(answerDTO.getAnswer())
                    .questionId(answerDTO.getQuestionId())
                    .gameId(answerDTO.getGameId())
                    .user(currentUserService.getUserCurrent())
                    .isCorrect(correct)
                    .answeredAt(LocalDateTime.now())
                    .point(correct ? point : 0)
                    .build();
            playerAnswerRepository.save(playerAnswer);

            result = AnswerResultDTO.builder()
                    .correct(correct)
                    .explanation(fb.getExplanation())
                    .build();

        } else {
            throw new RuntimeException("Unsupported gameId: " + answerDTO.getGameId());
        }

        // Kiểm tra nếu user hoàn thành game
        AnswerResultDTO completed = checkAndCompleteGame(answerDTO.getGameId());
        if (completed != null) {
            result.setTotalScore(completed.getTotalScore());
            result.setWrongCount(completed.getWrongCount());
        }

        return result;
    }

    @Override
    public StartGameResponse startGame(Long gameId, Long topicId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Not found game with id:" + gameId));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Not found topic with id: " + topicId));
        PlayerGame playerGame = PlayerGame.builder()
                .gameId(gameId)
                .userId(currentUserService.getUserCurrent().getId())
                .completed(false)
                .totalScore(0)
                .startAt(LocalDateTime.now())
                .build();
        playerGameRepository.save(playerGame);
        List<QuestionDTO> questions = multipleChoiceGameQuestionRepository.findByTopic(topic)
                .stream()
                .map(q -> QuestionDTO.builder()
                        .gameId(gameId)
                        .questionText(q.getQuestionText())
                        .image_url(q.getImage_url())
                        .options(optionRepository.findAllByQuestionId(q.getId()).stream()
                                .map(o -> new OptionDTO(o.getId(), o.getText()))
                                .toList())
                        .build()
                ).toList();
        return StartGameResponse.builder()
                .gameId(gameId)
                .playerGameId(playerGame.getId())
                .topicId(topic.getId())
                .questions(questions)
                .startTime(playerGame.getStartAt())
                .build();
    }


    private AnswerResultDTO checkAndCompleteGame(Long gameId) {
        Long userId = currentUserService.getUserCurrent().getId();
        int answeredCount = playerAnswerRepository.countByUserIdAndGameId(userId, gameId);
        long totalQuestion = 0;
        if (gameId == 1) {
            totalQuestion = multipleChoiceGameQuestionRepository.countByGameId(gameId);
        } else if (gameId == 2) {
            totalQuestion = fillBlankQuestionRepository.countByGameId(gameId);
        }
        if (answeredCount >= totalQuestion) {
            int totalScore = playerAnswerRepository.sumPointByUserIdAndGameId(userId, gameId);
            int wrongCount = answeredCount - (totalScore / 10);
            PlayerGame playerGame = PlayerGame
                    .builder()
                    .gameId(gameId)
                    .totalScore(totalScore)
                    .userId(userId)
                    .completed(true)
                    .completedAt(LocalDateTime.now())
                    .build();
            playerGameRepository.save(playerGame);
            return AnswerResultDTO.builder()
                    .totalScore(totalScore)
                    .complete(true)
                    .wrongCount(wrongCount)
                    .build();
        }
        return null;
    }
}
