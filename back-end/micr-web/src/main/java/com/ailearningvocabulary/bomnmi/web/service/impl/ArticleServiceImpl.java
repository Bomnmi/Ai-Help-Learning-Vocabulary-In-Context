package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabularyKey;
import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphService;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphVocabularyService;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.web.service.AiService;
import com.ailearningvocabulary.bomnmi.web.service.ArticleService;
import com.ailearningvocabulary.bomnmi.web.service.BaseService;
import com.ailearningvocabulary.bomnmi.web.service.NLPService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 14:41
 */
@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {

    @Value("${ai.chat-gpt.words-limit}")
    private Integer wordsLimit;

    /**
     * Map the vocabularies to the wordList
     * @param vocabularies
     * @return
     */
    @Override
    public List<Paragraph> generateParagraphByVocabularies(List<UserVocabulary> vocabularies, String userId) throws IOException, ProjectException{
        //Map the UserVocabulary list into word list
        List<String> wordList = vocabularies.stream().
                map(UserVocabularyKey::getWord).collect(Collectors.toList());

        //Shuffle the sequence of wordList
        Collections.shuffle(wordList);

        //slice the wordList into small partition according to the word limit.
        List<List<String>> contentRequest = CommonUtil.sliceArray(wordList, wordsLimit);

        //Connect with AiService and generate paragraph
        List<Paragraph> paragraphs = aiService.generateParagraphByWordList(contentRequest);

        //Add id, userId, readStatus, translation and current date to paragraphs
        for (Paragraph paragraph : paragraphs) {
            paragraph.setId(CommonUtil.generateUUID());
            paragraph.setUserId(userId);
            paragraph.setCreateTime(LocalDateTime.now());
            paragraph.setReadStatus(0);
            String translationContent = deepLTranslationService.queryTranslationOfParagraph(paragraph.getContent());
            paragraph.setTranslationContent(translationContent);
        }

        //Maybe first generate paragraph, there is not any vocabulary
        if(contentRequest.size() != 0){
            //Mark the vocabulary in paragraph.
            for (int i = 0; i < paragraphs.size(); i++) {
                String temp = coreNLPService.
                        markAddedVocabularyInParagraph(paragraphs.get(i).getContent(),
                                contentRequest.get(i));
                paragraphs.get(i).setMarkedContent(temp);
            }
        }else{
            for (Paragraph paragraph : paragraphs) {
                paragraph.setMarkedContent(paragraph.getContent());
            }
        }

        paragraphService.addParagraphAndVocabulary(paragraphs, contentRequest, userId);

        return paragraphs;
    }

    @Override
    public List<Paragraph> queryUnReadParagraphsByUserId(String userId) {
        return paragraphService.queryUnReadParagraphsByUserId(userId);
    }
}
