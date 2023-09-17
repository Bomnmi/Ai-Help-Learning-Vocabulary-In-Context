package com.ailearningvocabulary.bomnmi.api.model;

import java.io.Serializable;

public class FriendKey implements Serializable {
    private String userId;

    private String friendId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}