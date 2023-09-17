package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import com.ailearningvocabulary.bomnmi.api.service.VocabularyService;
import com.ailearningvocabulary.bomnmi.dataset.mappers.VocabularyMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/8 15:44
 */
@DubboService(interfaceClass = VocabularyService.class, version = "1.0")
public class VocabularyServiceImpl implements VocabularyService {

    @Autowired
    VocabularyMapper vocabularyMapper;

    @Override
    public Vocabulary queryVocabularyByWord(String word) {
        Vocabulary vocabulary = vocabularyMapper.selectCommonVocabulary(word);
        if (vocabulary == null) {
            vocabulary = vocabularyMapper.selectVocabularyByWord(word, word.substring(0, 1).toLowerCase());
        }
        return vocabulary;
    }

    @Override
    public List<Vocabulary> queryLimitCommonVocabularyExcludeSomeWords(int wordNumber, List<String> wordList) {
        return vocabularyMapper.selectLimitCommonVocabularyExcludeSomeWords(wordNumber, wordList);
    }

}
