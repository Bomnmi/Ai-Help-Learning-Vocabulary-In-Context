package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.service.UserVocabularyService;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.dataset.mappers.UserVocabularyMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 12:07
 */
@ContextConfiguration(classes = {UserVocabularyServiceImpl.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserVocabularyServiceTest {
    @MockBean
    UserVocabularyMapper userVocabularyMapper;

    @Autowired
    UserVocabularyService underTest;

    @Test
    void testSelectAllUserVocabularyByUserId(){

        underTest.queryAllUserVocabularyByUserId(CommonUtil.generateUUID());

        verify(userVocabularyMapper).selectAllWordsByUserId(any());
    }

    @Test
    void testAddWordIntoUserVocabulary(){
        underTest.addWordIntoUserVocabulary(CommonUtil.generateUUID(), "word");

        verify(userVocabularyMapper).insert(any());
    }

    @Test
    void testQueryUserVocabularyByPrimaryKey(){
        underTest.queryUserVocabularyByPrimaryKey(CommonUtil.generateUUID(), "word");

        verify(userVocabularyMapper).selectByPrimaryKey(any());
    }




}
