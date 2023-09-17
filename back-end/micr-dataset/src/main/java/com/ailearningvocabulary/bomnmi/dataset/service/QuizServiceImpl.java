package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.Quiz;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import com.ailearningvocabulary.bomnmi.api.service.QuizService;
import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.dataset.mappers.QuizMapper;
import com.ailearningvocabulary.bomnmi.dataset.mappers.UserVocabularyMapper;
import com.ailearningvocabulary.bomnmi.dataset.mappers.VocabularyMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/18 20:23
 */
@DubboService(interfaceClass = QuizService.class, version = "1.0")
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizMapper quizMapper;

    @Override
    public int insertQuizByUserIdAndScore(String userId, Double score) {
        Quiz quiz = new Quiz();
        quiz.setId(CommonUtil.generateUUID());
        quiz.setUserId(userId);
        quiz.setScore(score);
        quiz.setCreateTime(LocalDateTime.now());

        return quizMapper.insert(quiz);
    }

    @Override
    public List<Quiz> queryAllQuizByUserId(String userId) {
        return quizMapper.selectAllQuizByUserId(userId);
    }

}
