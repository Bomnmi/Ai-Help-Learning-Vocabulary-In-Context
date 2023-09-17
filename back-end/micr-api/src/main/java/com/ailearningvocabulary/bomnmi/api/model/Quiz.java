package com.ailearningvocabulary.bomnmi.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/18 20:22
 */
public class Quiz implements Serializable {
    private String id;

    private String userId;

    private Double score;

    private LocalDateTime createTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
