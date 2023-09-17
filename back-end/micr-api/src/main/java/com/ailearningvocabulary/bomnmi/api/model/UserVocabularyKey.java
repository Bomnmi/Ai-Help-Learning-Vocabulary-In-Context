package com.ailearningvocabulary.bomnmi.api.model;

import java.io.Serializable;

public class UserVocabularyKey implements Serializable {
    private String userId;

    private String word;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}