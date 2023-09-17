package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 14:44
 */
@SpringBootTest
public class ParagraphVocabularyMapperTest {
    @Autowired
    ParagraphVocabularyMapper underTest;

    @Test
    void testInsertWordsOfOneParagraph(){
        String paragraphId = CommonUtil.generateUUID();
        List<String> wordList = Arrays.asList("Hello", "world", "java", "computer");

        int test = underTest.insertWordsOfOneParagraph(paragraphId, wordList);

        assertThat(test).isEqualTo(wordList.size());
    }

    @Test
    void testSelectWordsByParagraphId(){
        String paragraphId = CommonUtil.generateUUID();
        List<String> wordList = Arrays.asList("Hello", "world", "java", "computer");
        underTest.insertWordsOfOneParagraph(paragraphId, wordList);

        List<String> test = underTest.selectWordsByParagraphId(paragraphId);
        assertThat(test.size()).isEqualTo(wordList.size());
    }
}

