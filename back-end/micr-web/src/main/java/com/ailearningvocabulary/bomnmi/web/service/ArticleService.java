package com.ailearningvocabulary.bomnmi.web.service;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;

import java.io.IOException;
import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 14:39
 */
public interface ArticleService {

    List<Paragraph> generateParagraphByVocabularies(List<UserVocabulary> vocabularies, String userId) throws IOException, ProjectException;

    List<Paragraph> queryUnReadParagraphsByUserId(String userId);

}
