package com.ailearningvocabulary.bomnmi.web.service;

import com.ailearningvocabulary.bomnmi.api.service.ParagraphService;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphVocabularyService;
import com.ailearningvocabulary.bomnmi.api.service.UserVocabularyService;
import com.ailearningvocabulary.bomnmi.api.service.VocabularyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/8 15:55
 */
public class BaseService {

    @DubboReference(interfaceClass = ParagraphService.class, version = "1.0")
    protected ParagraphService paragraphService;

    @DubboReference(interfaceClass = ParagraphVocabularyService.class, version = "1.0")
    protected ParagraphVocabularyService paragraphVocabularyService;

    @DubboReference(interfaceClass = VocabularyService.class, version = "1.0")
    protected VocabularyService vocabularyService;

    @DubboReference(interfaceClass = UserVocabularyService.class, version = "1.0")
    protected UserVocabularyService userVocabularyService;

    @Autowired
    @Qualifier("deepLTranslationService")
    protected TranslationService deepLTranslationService;

    @Autowired
    protected NLPService coreNLPService;

    @Autowired
    protected AiService aiService;

    @Autowired
    @Qualifier("freeDictionaryService")
    protected TranslationService freeDictionaryService;
}
