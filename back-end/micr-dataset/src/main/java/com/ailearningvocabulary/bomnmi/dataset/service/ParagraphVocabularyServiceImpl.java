package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphService;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphVocabularyService;
import com.ailearningvocabulary.bomnmi.dataset.mappers.ParagraphVocabularyMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 14:38
 */
@DubboService(interfaceClass = ParagraphVocabularyService.class, version = "1.0")
public class ParagraphVocabularyServiceImpl implements ParagraphVocabularyService {

    @Autowired
    ParagraphVocabularyMapper paragraphVocabularyMapper;

    @Override
    public int addParagraphVocabulary(String paragraphId, List<String> wordList) {
        return paragraphVocabularyMapper.insertWordsOfOneParagraph(paragraphId, wordList);
    }

    @Override
    public List<String> queryWordsByParagraphId(String paragraphId) {
        return paragraphVocabularyMapper.selectWordsByParagraphId(paragraphId);
    }

    @Override
    public List<String> queryUnReviewedWordsByParagraphId(String paragraphId) {
        return paragraphVocabularyMapper.selectUnReviewedWordByParagraphId(paragraphId);
    }

}
