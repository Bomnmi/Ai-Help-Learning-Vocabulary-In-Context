package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabularyKey;
import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserVocabularyMapper {
    int deleteByPrimaryKey(UserVocabularyKey key);

    int insert(UserVocabulary record);

    int insertSelective(UserVocabulary record);

    UserVocabulary selectByPrimaryKey(UserVocabularyKey key);

    int updateByPrimaryKeySelective(UserVocabulary record);

    int updateByPrimaryKey(UserVocabulary record);

    List<UserVocabulary> selectAllWordsByUserId(@Param("userId") String userId);

    List<UserVocabulary> selectReviewNeedWordsByUserIdByNextReviewTime(@Param("userId") String userId);

    List<UserVocabulary> selectTodayAddedWordsByUserId(@Param("userId") String userId);

    List<UserVocabulary> selectTodayLearnedWordsByUserId(@Param("userId") String userId);

    int updateBatchUserVocabulary(List<UserVocabulary> userVocabularies);

    List<UserVocabulary> selectRecentWordsByUserId(@Param("userId") String userId,
                              @Param("days") Integer recentDays);

    List<UserVocabulary> selectWordsByQuizSetting(@Param("userId") String userId, @Param("quizSetting") QuizSetting quizSetting);
}