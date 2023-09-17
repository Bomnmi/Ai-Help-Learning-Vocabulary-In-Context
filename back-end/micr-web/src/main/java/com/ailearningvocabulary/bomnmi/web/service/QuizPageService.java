package com.ailearningvocabulary.bomnmi.web.service;

import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;
import com.ailearningvocabulary.bomnmi.web.view.ResponseQuizProblem;

import java.util.List;

public interface QuizPageService {

    List<ResponseQuizProblem> generateQuizProblem(String userId, QuizSetting quizSetting);
}
