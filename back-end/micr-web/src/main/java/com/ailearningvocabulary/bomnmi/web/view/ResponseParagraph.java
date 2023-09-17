package com.ailearningvocabulary.bomnmi.web.view;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;

import java.util.List;
import java.util.Map;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/7 20:36
 */
public class ResponseParagraph {

    private List<Paragraph> paragraphs;

    private Map<String, List<String>> wordList;

    public Map<String, List<String>> getWordList() {
        return wordList;
    }

    public void setWordList(Map<String, List<String>> wordList) {
        this.wordList = wordList;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }


}
