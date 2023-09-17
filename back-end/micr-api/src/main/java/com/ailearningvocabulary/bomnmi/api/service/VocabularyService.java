package com.ailearningvocabulary.bomnmi.api.service;

import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/8 15:44
 */
public interface VocabularyService {

    Vocabulary queryVocabularyByWord(String word);

    List<Vocabulary> queryLimitCommonVocabularyExcludeSomeWords(int wordNumber, List<String> wordList);
}
