package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphService;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphVocabularyService;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.dataset.mappers.ParagraphMapper;
import com.ailearningvocabulary.bomnmi.dataset.mappers.ParagraphVocabularyMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 13:59
 */
@ContextConfiguration(classes = {ParagraphServiceImpl.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ParagraphServiceTest {

    @MockBean
    ParagraphMapper paragraphMapper;

    @MockBean
    ParagraphVocabularyMapper paragraphVocabularyMapper;

    @Autowired
    ParagraphService underTest;

    @Test
    void testAddParagraph(){
        List<Paragraph> paragraphList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Paragraph paragraph = new Paragraph();
            paragraph.setCreateTime(LocalDateTime.now());
            paragraph.setId(CommonUtil.generateUUID());
            paragraph.setUserId(CommonUtil.generateUUID());
            paragraph.setContent("123");
            paragraph.setTitle("123");
            paragraphList.add(paragraph);
        }

        Integer integer = underTest.addParagraph(paragraphList);
        verify(paragraphMapper).insertBatchParagraphs(any());
    }

    @Test
    void testQueryUnReadParagraphsByUserId(){
        underTest.queryUnReadParagraphsByUserId(CommonUtil.generateUUID());
        verify(paragraphMapper).selectUnReadParagraphByUserId(any());
    }

}
