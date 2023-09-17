package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.User;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User user);


    User selectUserByEmailAndPassword(@Param("email") String email,
                                      @Param("password") String password);

    int updateUserLastLoginTimeById(@Param("id") String id, @Param("today") LocalDateTime today);

    User selectUserByEmail(@Param("email") String email);

    int insertNewUser(User user);

    User selectUserById(@Param("id") String id);

    int updateSelectiveUser(User user);


}