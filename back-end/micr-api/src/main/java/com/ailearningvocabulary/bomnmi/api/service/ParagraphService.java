package com.ailearningvocabulary.bomnmi.api.service;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 13:30
 */
public interface ParagraphService {

    int addParagraph(List<Paragraph> paragraphs);

    void addParagraphAndVocabulary(List<Paragraph> paragraphs, List<List<String>> contentRequest, String userId);

    List<Paragraph> queryUnReadParagraphsByUserId(String userId);

    Paragraph queryParagraphByParagraphId(String paragraphId);

    void updateParagraphStatusAndUserVocabularyStatus(String userId, String paragraphId);

}
