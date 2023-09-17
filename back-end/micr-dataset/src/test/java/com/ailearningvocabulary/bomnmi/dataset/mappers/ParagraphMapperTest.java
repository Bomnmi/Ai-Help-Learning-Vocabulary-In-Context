package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
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
 * @date 2023/8/5 13:44
 */
@SpringBootTest
public class ParagraphMapperTest {

    @Autowired
    ParagraphMapper underTest;

    @Test
    void testInsertBatchParagraphs(){
        List<Paragraph> paragraphList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Paragraph paragraph = new Paragraph();
            paragraph.setCreateTime(LocalDateTime.now());
            paragraph.setId(CommonUtil.generateUUID());
            paragraph.setUserId(CommonUtil.generateUUID());
            paragraph.setContent("123");
            paragraph.setTitle("123");
            paragraph.setReadStatus(0);
            paragraph.setMarkedContent("MarkedContent");
            paragraphList.add(paragraph);
        }
        int test = underTest.insertBatchParagraphs(paragraphList);
        assertThat(test).isEqualTo(3);
    }

    @Test
    void testSelectUnReadParagraphByUserId(){
        List<Paragraph> paragraphList = new ArrayList<>();
        String userId = CommonUtil.generateUUID();
        for (int i = 0; i < 6; i++) {
            Paragraph paragraph = new Paragraph();
            paragraph.setCreateTime(LocalDateTime.now());
            paragraph.setId(CommonUtil.generateUUID());
            paragraph.setUserId(userId);
            paragraph.setContent("Content");
            paragraph.setTitle("Title");
            paragraph.setReadStatus(0);
            paragraphList.add(paragraph);
        }

        for (int i = 0; i < 3; i++) {
            Paragraph paragraph = new Paragraph();
            paragraph.setCreateTime(LocalDateTime.now());
            paragraph.setId(CommonUtil.generateUUID());
            paragraph.setUserId(userId);
            paragraph.setContent("Content");
            paragraph.setTitle("Title");
            paragraph.setReadStatus(1);
            paragraphList.add(paragraph);
        }

        underTest.insertBatchParagraphs(paragraphList);

        List<Paragraph> test = underTest.selectUnReadParagraphByUserId(userId);
        for (Paragraph paragraph : test) {
            assertThat(paragraph.getReadStatus()).isEqualTo(0);
        }
    }

    @Test
    void testSelectParagraphByParagraphId(){
        List<Paragraph> paragraphList = new ArrayList<>();
        String paragraphId = CommonUtil.generateUUID();
        String userId = CommonUtil.generateUUID();
        Paragraph paragraph = new Paragraph();
        paragraph.setCreateTime(LocalDateTime.now());
        paragraph.setId(paragraphId);
        paragraph.setUserId(userId);
        paragraph.setContent("Content");
        paragraph.setTitle("Title");
        paragraph.setReadStatus(0);
        paragraphList.add(paragraph);

        for (int i = 0; i < 6; i++) {
            paragraph = new Paragraph();
            paragraph.setCreateTime(LocalDateTime.now());
            paragraph.setId(CommonUtil.generateUUID());
            paragraph.setUserId(userId);
            paragraph.setContent("Content");
            paragraph.setTitle("Title");
            paragraph.setReadStatus(0);
            paragraphList.add(paragraph);
        }

        for (int i = 0; i < 3; i++) {
            paragraph = new Paragraph();
            paragraph.setCreateTime(LocalDateTime.now());
            paragraph.setId(CommonUtil.generateUUID());
            paragraph.setUserId(userId);
            paragraph.setContent("Content");
            paragraph.setTitle("Title");
            paragraph.setReadStatus(1);
            paragraphList.add(paragraph);
        }

        underTest.insertBatchParagraphs(paragraphList);

        Paragraph test = underTest.selectParagraphByParagraphId(paragraphId);
        assertThat(test.getContent()).isEqualTo("Content");
    }

    @Test
    void testUpdateSelectiveParagraphByParagraphId(){
        List<Paragraph> paragraphList = new ArrayList<>();
        String paragraphId = CommonUtil.generateUUID();
        String userId = CommonUtil.generateUUID();
        Paragraph paragraph = new Paragraph();
        paragraph.setCreateTime(LocalDateTime.now());
        paragraph.setId(paragraphId);
        paragraph.setUserId(userId);
        paragraph.setContent("Content");
        paragraph.setTitle("Title");
        paragraph.setReadStatus(0);
        paragraphList.add(paragraph);
        underTest.insertBatchParagraphs(paragraphList);

        paragraph.setReadStatus(1);
        int rows = underTest.updateSelectiveParagraph(paragraph);
        Paragraph test = underTest.selectParagraphByParagraphId(paragraphId);
        assertThat(test.getReadStatus()).isEqualTo(1);
        assertThat(rows).isEqualTo(1);
    }


}
