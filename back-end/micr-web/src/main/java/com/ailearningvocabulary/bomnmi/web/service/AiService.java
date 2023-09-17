package com.ailearningvocabulary.bomnmi.web.service;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 19:06
 */
public interface AiService {

    List<Paragraph> generateParagraphByWordList(List<List<String>> wordList);
}
