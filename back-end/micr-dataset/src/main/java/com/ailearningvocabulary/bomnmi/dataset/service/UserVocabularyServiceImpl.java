package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabularyKey;
import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;
import com.ailearningvocabulary.bomnmi.api.service.UserVocabularyService;
import com.ailearningvocabulary.bomnmi.api.service.VocabularyService;
import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.dataset.mappers.UserVocabularyMapper;
import com.ailearningvocabulary.bomnmi.dataset.mappers.VocabularyMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 12:06
 */
@DubboService(interfaceClass = UserVocabularyService.class, version = "1.0")
@CacheConfig(cacheNames = "userVocabulary")
public class UserVocabularyServiceImpl implements UserVocabularyService {

    @Autowired
    UserVocabularyMapper userVocabularyMapper;

    @Autowired
    VocabularyService vocabularyService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Cacheable(key="'ALL:WORD:USER:ID' + #p0")
    public List<UserVocabulary> queryAllUserVocabularyByUserId(String userId) {
        System.out.println("Cache not exist");
        return userVocabularyMapper.selectAllWordsByUserId(userId);
    }

    @Override
    //@Cacheable(key="'REVIEW:WORD:USER:ID:' + #p0" )
    public List<UserVocabulary> queryReviewNeedUserVocabularyByUserIdByNextReviewTime(String userId) {
        System.out.println("Cache not exist");
        return userVocabularyMapper.selectReviewNeedWordsByUserIdByNextReviewTime(userId);
    }

    @Override
    @CachePut(key="'ALL:WORD:USER:ID' + #p0")
    public List<UserVocabulary> addWordIntoUserVocabulary(String userId, String word) {
        UserVocabulary userVocabulary = new UserVocabulary();
        //Initialize the userVocabulary
        userVocabulary.setUserId(userId);
        userVocabulary.setWord(word);
        userVocabulary.setAddTime(LocalDateTime.now());
        userVocabulary.setReviewTimes(0);
        userVocabulary.setNextReviewTime(LocalDateTime.now());
        userVocabulary.setEf(2.5);
        userVocabulary.setUsed(0);

        //also need to get the translation, definition and pronunciation of this word
        Vocabulary vocabulary = vocabularyService.queryVocabularyByWord(word);
        userVocabulary.setDefinition(vocabulary.getDefinition());
        userVocabulary.setTranslation(vocabulary.getTranslation());
        String[] pronunciationUrls = CommonUtil.youDaoPronunciationUrl(word);
        userVocabulary.setPronunciation(pronunciationUrls[0] + "\\" + pronunciationUrls[1]);

        //update databse
        int rows = userVocabularyMapper.insertSelective(userVocabulary);
        if (rows != 1) {
            throw new ProjectException(ResultCode.SYSTEM_INNER_ERROR);
        }

        //update the Redis Cache
        List<UserVocabulary> userVocabularies =
                (List<UserVocabulary>) redisTemplate.
                        opsForValue().
                        get("userVocabulary::ALL:WORD:USER:ID" + userId);
        userVocabularies.add(userVocabulary);
        return userVocabularies;
    }

    @Override
    public List<UserVocabulary> queryTodayWordsByUserId(String userId) {
        return userVocabularyMapper.selectTodayAddedWordsByUserId(userId);
    }

    @Override
    public UserVocabulary queryUserVocabularyByPrimaryKey(String userId, String word) {
        UserVocabularyKey key = new UserVocabularyKey();
        key.setUserId(userId);
        key.setWord(word);
        return userVocabularyMapper.selectByPrimaryKey(key);
    }

    @Override
    @CacheEvict(key="'ALL:WORD:USER:ID' + #userVocabulary.userId")
    public int updateUserVocabularyByPrimaryKey(UserVocabulary userVocabulary) {
        return userVocabularyMapper.updateByPrimaryKey(userVocabulary);
    }

    @Override
    public List<UserVocabulary> queryTodayLearnedWords(String userId) {
        return  userVocabularyMapper.selectTodayLearnedWordsByUserId(userId);
    }

    @Override
    public List<UserVocabulary> queryTodayLearnedWords(List<UserVocabulary> userVocabularies) {
        List<UserVocabulary> result = new ArrayList<>();
        for (UserVocabulary userVocabulary : userVocabularies) {
            LocalDateTime lastReviewTime = userVocabulary.getLastReviewTime();
            if ((lastReviewTime != null) &&
                    lastReviewTime.toLocalDate().compareTo(LocalDate.now()) == 0) {
                result.add(userVocabulary);
            }
        }
        return result;
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateBatchUserVocabulary(List<UserVocabulary> userVocabularies) {
        return userVocabularyMapper.updateBatchUserVocabulary(userVocabularies);
    }

    @Override
    public List<UserVocabulary> queryRecentWordsByUserId(String userId, Integer days) {
        return userVocabularyMapper.selectRecentWordsByUserId(userId, days);
    }

    @Override
    public List<UserVocabulary> queryWordsByQuizSetting(String userId, QuizSetting quizSetting) {
        return userVocabularyMapper.selectWordsByQuizSetting(userId, quizSetting);
    }

}
