package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.Friend;
import com.ailearningvocabulary.bomnmi.api.model.FriendKey;


public interface FriendMapper {
    int deleteByPrimaryKey(FriendKey key);

    int insert(Friend record);

    int insertSelective(Friend record);

    Friend selectByPrimaryKey(FriendKey key);

    int updateByPrimaryKeySelective(Friend record);

    int updateByPrimaryKey(Friend record);
}