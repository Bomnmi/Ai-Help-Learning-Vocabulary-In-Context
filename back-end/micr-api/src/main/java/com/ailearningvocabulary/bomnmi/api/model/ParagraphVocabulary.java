package com.ailearningvocabulary.bomnmi.api.model;

import java.io.Serializable;

public class ParagraphVocabulary implements Serializable {
    private String paragraphId;



    private String word;

    public String getParagraphId() {
        return paragraphId;
    }

    public void setParagraphId(String paragraphId) {
        this.paragraphId = paragraphId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}