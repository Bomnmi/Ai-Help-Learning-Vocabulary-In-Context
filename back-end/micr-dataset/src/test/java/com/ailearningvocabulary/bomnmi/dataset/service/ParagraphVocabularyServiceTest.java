package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.service.ParagraphService;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphVocabularyService;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.dataset.mappers.ParagraphVocabularyMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 14:48
 */
@ContextConfiguration(classes = {ParagraphVocabularyServiceImpl.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ParagraphVocabularyServiceTest {

    @MockBean
    ParagraphVocabularyMapper paragraphVocabularyMapper;

    @Autowired
    ParagraphVocabularyService underTest;

    @Test
    void testAddParagraphVocabulary(){
        String paragraphId = CommonUtil.generateUUID();
        List<String> wordList = Arrays.asList("Hello", "world", "java", "computer");

        int rows = underTest.addParagraphVocabulary(paragraphId, wordList);
        verify(paragraphVocabularyMapper).insertWordsOfOneParagraph(any(), any());
    }

    @Test
    void testQueryWordsByParagraphId(){
        underTest.queryWordsByParagraphId(CommonUtil.generateUUID());

        verify(paragraphVocabularyMapper).selectWordsByParagraphId(any());
    }
}
