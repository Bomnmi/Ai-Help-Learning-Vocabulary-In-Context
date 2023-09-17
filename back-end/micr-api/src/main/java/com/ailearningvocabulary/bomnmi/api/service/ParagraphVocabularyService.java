package com.ailearningvocabulary.bomnmi.api.service;

import java.util.List;

public interface ParagraphVocabularyService {

    int addParagraphVocabulary(String paragraphId, List<String> wordList);

    List<String> queryWordsByParagraphId(String paragraphId);

    List<String> queryUnReviewedWordsByParagraphId(String paragraphId);
}
