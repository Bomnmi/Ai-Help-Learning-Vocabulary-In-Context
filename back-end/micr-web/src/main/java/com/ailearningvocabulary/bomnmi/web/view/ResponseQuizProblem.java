package com.ailearningvocabulary.bomnmi.web.view;

import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/19 20:47
 */
public class ResponseQuizProblem {
    private List<Vocabulary> vocabularyList;
    private String problemDefinition;
    private String correctWord;

    //-1 means does not answer yet
    //0 means wrong
    //1 means correct
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<Vocabulary> getVocabularyList() {
        return vocabularyList;
    }

    public void setVocabularyList(List<Vocabulary> vocabularyList) {
        this.vocabularyList = vocabularyList;
    }

    public String getProblemDefinition() {
        return problemDefinition;
    }

    public void setProblemDefinition(String problemDefinition) {
        this.problemDefinition = problemDefinition;
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }
}
