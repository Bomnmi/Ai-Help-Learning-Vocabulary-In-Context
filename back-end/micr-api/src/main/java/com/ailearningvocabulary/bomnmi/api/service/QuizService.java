package com.ailearningvocabulary.bomnmi.api.service;


import com.ailearningvocabulary.bomnmi.api.model.Quiz;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;

import java.util.List;

public interface QuizService {

    int insertQuizByUserIdAndScore(String userId, Double score);

    List<Quiz> queryAllQuizByUserId(String userId);

}
