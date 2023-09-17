package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.Quiz;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuizMapper {

    int insert(Quiz quiz);

    List<Quiz> selectAllQuizByUserId(@Param("userId") String userId);

}
