package com.ailearningvocabulary.bomnmi.web.service;

import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;

import java.io.IOException;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/8 16:05
 */
public interface TranslationService {

    default String queryTranslationOrDefinition(String word) throws IOException {
        return null;
    }

    default String queryTranslationOfParagraph(String content) throws IOException {
        return null;
    }

    default Vocabulary queryLocalRepositoryWord(String word){
        return null;
    }
}
