package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabularyKey;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 11:45
 */
@SpringBootTest
public class UserVocabularyMapperTest {

    @Autowired
    UserVocabularyMapper underTest;

    @Test
    void testSelectAllWordsByUserId(){
        String userId = CommonUtil.generateUUID();
        UserVocabulary userVocabulary = new UserVocabulary();
        String[] wordList = new String[]{"hello", "world", "computer"};
        userVocabulary.setUserId(userId);
        userVocabulary.setWord(wordList[0]);
        userVocabulary.setNextReviewTime(LocalDateTime.now().plusDays(1L));
        userVocabulary.setLastReviewTime(LocalDateTime.now().plusDays(-1));
        underTest.insertSelective(userVocabulary);

        userVocabulary.setWord(wordList[1]);
        userVocabulary.setLastReviewTime(LocalDateTime.now());
        underTest.insertSelective(userVocabulary);

        userVocabulary.setWord(wordList[2]);
        userVocabulary.setLastReviewTime(LocalDateTime.now());
        underTest.insertSelective(userVocabulary);

        List<UserVocabulary> test = underTest.selectReviewNeedWordsByUserIdByNextReviewTime(userVocabulary.getUserId());
        for (int i = 0; i < test.size(); i++) {
            assertThat(test.get(i).getUserId()).isEqualTo(userId);
        }

    }

    @Test
    void testInsert(){
        UserVocabulary userVocabulary = new UserVocabulary();
        userVocabulary.setUserId(CommonUtil.generateUUID());
        userVocabulary.setWord("word");
        userVocabulary.setReviewTimes(1);
        userVocabulary.setNextReviewTime(LocalDateTime.now());
        userVocabulary.setAddTime(LocalDateTime.now());
        userVocabulary.setUsed(0);
        userVocabulary.setTranslation("123");
        userVocabulary.setDefinition("123");
        userVocabulary.setPronunciation("123");

        int test = underTest.insert(userVocabulary);
        assertThat(test).isEqualTo(1);
    }

    @Test
    void testSelectByPrimaryKey(){
        UserVocabulary userVocabulary = new UserVocabulary();
        String userId = CommonUtil.generateUUID();
        userVocabulary.setUserId(userId);
        userVocabulary.setWord("word");
        userVocabulary.setReviewTimes(1);
        userVocabulary.setNextReviewTime(LocalDateTime.now());
        userVocabulary.setAddTime(LocalDateTime.now());
        userVocabulary.setUsed(0);
        userVocabulary.setDefinition("123123");
        userVocabulary.setTranslation("123123");
        userVocabulary.setPronunciation("123123123");

        underTest.insert(userVocabulary);
        UserVocabularyKey userVocabularyKey = new UserVocabularyKey();
        userVocabularyKey.setUserId(userId);
        userVocabularyKey.setWord("word");
        UserVocabulary test = underTest.selectByPrimaryKey(userVocabularyKey);
        assertThat(test.getUserId()).isEqualTo(userId);
        assertThat(test.getDefinition()).isEqualTo(userVocabulary.getDefinition());
        assertThat(test.getTranslation()).isEqualTo(userVocabulary.getTranslation());
        assertThat(test.getPronunciation()).isEqualTo(userVocabulary.getPronunciation());
    }

    @Test
    void testUpdateByPrimaryKey(){
        UserVocabulary userVocabulary = new UserVocabulary();
        String userId = CommonUtil.generateUUID();
        userVocabulary.setUserId(userId);
        userVocabulary.setWord("word");
        userVocabulary.setReviewTimes(1);
        userVocabulary.setNextReviewTime(LocalDateTime.now());
        userVocabulary.setAddTime(LocalDateTime.now());
        userVocabulary.setUsed(0);
        userVocabulary.setEf(2.5);

        int insert = underTest.insert(userVocabulary);
        userVocabulary.setEf(3d);
        int rows = underTest.updateByPrimaryKeySelective(userVocabulary);
        UserVocabularyKey userVocabularyKey = new UserVocabularyKey();
        userVocabularyKey.setWord("word");
        userVocabularyKey.setUserId(userId);

        UserVocabulary test = underTest.selectByPrimaryKey(userVocabularyKey);
        assertThat(rows).isEqualTo(1);
        assertThat(test.getEf()).isEqualTo(3d);


    }

    @Test
    void testUpdateBatchUserVocabulary(){
        List<UserVocabulary> userVocabularies = new ArrayList<>();
        String userId = CommonUtil.generateUUID();
        for (int i = 0; i < 10; i++) {
            UserVocabulary userVocabulary = new UserVocabulary();
            userVocabulary.setUserId(userId);
            userVocabulary.setWord("word" + i);
            userVocabulary.setReviewTimes(1);
            userVocabulary.setNextReviewTime(LocalDateTime.now());
            userVocabulary.setAddTime(LocalDateTime.now());
            userVocabulary.setUsed(0);

            underTest.insert(userVocabulary);
        }
        for (int i = 0; i < 10; i++) {
            UserVocabulary userVocabulary = new UserVocabulary();
            userVocabulary.setUserId(userId);
            userVocabulary.setWord("word" + i);
            userVocabulary.setUsed(1);
            userVocabularies.add(userVocabulary);
        }

        int rows = underTest.updateBatchUserVocabulary(userVocabularies);

        //assertThat(rows).isEqualTo(10);
        List<UserVocabulary> test = underTest.selectAllWordsByUserId(userId);
        for (UserVocabulary userVocabulary : test) {
            assertThat(userVocabulary.getUsed()).isEqualTo(1);
        }


    }
}
