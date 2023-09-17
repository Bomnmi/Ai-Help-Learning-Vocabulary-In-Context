package com.ailearningvocabulary.bomnmi.api.service;

import com.ailearningvocabulary.bomnmi.api.model.User;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabularyKey;
import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;

import java.util.List;

public interface UserVocabularyService {

    List<UserVocabulary> queryAllUserVocabularyByUserId(String userId);

    List<UserVocabulary> queryReviewNeedUserVocabularyByUserIdByNextReviewTime(String userId);

    List<UserVocabulary> addWordIntoUserVocabulary(String userId, String word);

    List<UserVocabulary> queryTodayWordsByUserId(String userId);

    UserVocabulary queryUserVocabularyByPrimaryKey(String userId, String word);

    int updateUserVocabularyByPrimaryKey(UserVocabulary userVocabulary);

    List<UserVocabulary> queryTodayLearnedWords(String userId);

    List<UserVocabulary> queryTodayLearnedWords(List<UserVocabulary> userVocabularies);

    int updateBatchUserVocabulary(List<UserVocabulary> userVocabularies);

    List<UserVocabulary> queryRecentWordsByUserId(String userId, Integer days);

    List<UserVocabulary> queryWordsByQuizSetting(String userId, QuizSetting quizSetting);
}
