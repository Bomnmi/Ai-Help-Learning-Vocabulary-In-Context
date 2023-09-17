package com.ailearningvocabulary.bomnmi.dataset;

import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.service.UserVocabularyService;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.dataset.mappers.UserVocabularyMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MicrDatasetApplicationTests {

    @Autowired
    UserVocabularyMapper underTest;

    @Autowired
    UserVocabularyService userVocabularyService;



    @Test
    void testSelectAllWordsByUserId(){
        String userId = CommonUtil.generateUUID();
        UserVocabulary userVocabulary = new UserVocabulary();
        String[] wordList = new String[]{"hello", "world", "computer"};
        userVocabulary.setUserId(userId);
        userVocabulary.setWord(wordList[0]);
        userVocabulary.setNextReviewTime(LocalDateTime.now());
        underTest.insertSelective(userVocabulary);

        userVocabulary.setWord(wordList[1]);
        userVocabulary.setNextReviewTime(LocalDateTime.now().plusDays(2));
        underTest.insertSelective(userVocabulary);

        userVocabulary.setWord(wordList[2]);
        userVocabulary.setNextReviewTime(LocalDateTime.now().plusDays(3));
        underTest.insertSelective(userVocabulary);

        List<UserVocabulary> userVocabularies = userVocabularyService.queryReviewNeedUserVocabularyByUserIdByNextReviewTime(CommonUtil.generateUUID());
        userVocabularyService.queryReviewNeedUserVocabularyByUserIdByNextReviewTime(userId);

        System.out.println(userVocabularies.stream().map(UserVocabulary::getEf).collect(Collectors.toList()));
    }

    @Test
    void testNullListAddIntoRedis() {
        String userId = CommonUtil.generateUUID();
        userVocabularyService.queryAllUserVocabularyByUserId(userId);
        userVocabularyService.queryAllUserVocabularyByUserId(userId);
    }

    @Test
    void testReflect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> aClass = UserVocabulary.class;
        Method getEf = aClass.getDeclaredMethod("getEf");
        Method getReviewTimes = aClass.getDeclaredMethod("getReviewTimes");
        UserVocabulary userVocabulary = new UserVocabulary();
        userVocabulary.setEf(2.5);

        System.out.println(getEf.invoke(userVocabulary));

    }



}
