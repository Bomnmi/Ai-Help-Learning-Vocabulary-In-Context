package com.ailearningvocabulary.bomnmi.api.model;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class UserVocabulary extends UserVocabularyKey implements Serializable {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime addTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastReviewTime;

    private Integer reviewTimes;

    private Double ef;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime nextReviewTime;

    private Integer used;

    private String translation;
    private String definition;

    private String pronunciation;

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "UserVocabulary{" +
                "addTime=" + addTime +
                ", lastReviewTime=" + lastReviewTime +
                ", reviewTimes=" + reviewTimes +
                ", ef=" + ef +
                ", nextReviewTime=" + nextReviewTime +
                '}';
    }

    public Double getEf() {
        return ef;
    }

    public void setEf(Double ef) {
        this.ef = ef;
    }

    public LocalDateTime getNextReviewTime() {
        return nextReviewTime;
    }

    public void setNextReviewTime(LocalDateTime nextReviewTime) {
        this.nextReviewTime = nextReviewTime;
    }

    public LocalDateTime getAddTime() {
        return addTime;
    }

    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }

    public LocalDateTime getLastReviewTime() {
        return lastReviewTime;
    }

    public void setLastReviewTime(LocalDateTime lastReviewTime) {
        this.lastReviewTime = lastReviewTime;
    }

    public Integer getReviewTimes() {
        return reviewTimes;
    }

    public void setReviewTimes(Integer reviewTimes) {
        this.reviewTimes = reviewTimes;
    }
}