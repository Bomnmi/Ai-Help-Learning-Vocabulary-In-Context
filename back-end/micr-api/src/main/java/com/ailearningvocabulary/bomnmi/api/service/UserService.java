package com.ailearningvocabulary.bomnmi.api.service;


import com.ailearningvocabulary.bomnmi.api.model.User;

import java.time.LocalDateTime;

public interface UserService {

    User queryUserEmailAndPasswordAreCorrect(String email, String password);

    int updateUserLoginTime(String id);

    boolean checkEmailIsRegistered(String email);

    int registerNewUser(String email, String password);

    User queryUserById(String id);

    int updateSelectiveUser(User user);

}
