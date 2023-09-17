package com.ailearningvocabulary.bomnmi.web.controller;

import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.service.*;
import com.ailearningvocabulary.bomnmi.web.service.*;
import com.ailearningvocabulary.bomnmi.web.service.ArticleService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/27 22:07
 */
public class BaseController {
    @Value("${ai.recent-days}")
    protected Integer recentDays;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected TokenService tokenService;

    @Autowired
    protected ArticleService articleService;

    @Autowired
    protected AiService aiService;

    @Autowired
    protected NLPService nlpService;

    @Autowired
    @Qualifier("freeDictionaryService")
    protected TranslationService freeDictionaryService;

    @Autowired
    @Qualifier("deepLTranslationService")
    protected TranslationService deepLTranslationService;

    @Autowired
    @Qualifier("superMemo2SpaceRepetitionService")
    protected SpaceRepetitionService superMemo2SpaceRepetitionService;

    @Autowired
    protected QuizPageService quizPageService;

    @Autowired
    @Qualifier("translationService")
    protected TranslationService translationService;

    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    protected UserService userService;

    @DubboReference(interfaceClass = RecordService.class, version = "1.0")
    protected RecordService recordService;

    @DubboReference(interfaceClass = UserVocabularyService.class, version = "1.0")
    protected UserVocabularyService userVocabularyService;

    @DubboReference(interfaceClass = ParagraphService.class, version = "1.0")
    protected ParagraphService paragraphService;

    @DubboReference(interfaceClass = ParagraphVocabularyService.class, version = "1.0")
    protected ParagraphVocabularyService paragraphVocabularyService;

    @DubboReference(interfaceClass = QuizService.class, version = "1.0")
    protected QuizService quizService;
}
