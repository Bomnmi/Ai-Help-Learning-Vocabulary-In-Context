package com.ailearningvocabulary.bomnmi.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Friend extends FriendKey implements Serializable {
    private LocalDateTime addTime;

    public LocalDateTime getAddTime() {
        return addTime;
    }

    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }
}