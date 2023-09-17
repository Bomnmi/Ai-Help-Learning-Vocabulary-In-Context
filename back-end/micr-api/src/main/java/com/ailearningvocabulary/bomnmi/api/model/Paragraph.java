package com.ailearningvocabulary.bomnmi.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Paragraph implements Serializable {
    private String id;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String title;

    private LocalDateTime createTime;

    private String content;

    private Integer readStatus;

    private String markedContent;

    private String translationContent;


    public String getTranslationContent() {
        return translationContent;
    }

    public void setTranslationContent(String translationContent) {
        this.translationContent = translationContent;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getMarkedContent() {
        return markedContent;
    }

    public void setMarkedContent(String markedContent) {
        this.markedContent = markedContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}