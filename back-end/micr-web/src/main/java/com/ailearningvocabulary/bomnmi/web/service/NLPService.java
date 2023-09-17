package com.ailearningvocabulary.bomnmi.web.service;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/6 22:51
 */
public interface NLPService {

    String markAddedVocabularyInParagraph(String content, List<String> wordList);

    List<String> getLemmatizationWordList(List<String> origin);

    String getLemmatizationWord(String origin);
}
