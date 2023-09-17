package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.web.service.BaseService;
import com.ailearningvocabulary.bomnmi.web.service.TranslationService;
import org.springframework.stereotype.Service;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/8 16:06
 */
@Service("translationService")
public class TranslationServiceImpl extends BaseService implements TranslationService {
    @Override
    public String queryTranslationOrDefinition(String word) {
        Vocabulary vocabulary = vocabularyService.queryVocabularyByWord(word);
        if (vocabulary == null) {
            throw new ProjectException("Doesn't have this word");
        }
        return vocabulary.getTranslation();
    }

    @Override
    public Vocabulary queryLocalRepositoryWord(String word) {
        return vocabularyService.queryVocabularyByWord(word);
    }
}
