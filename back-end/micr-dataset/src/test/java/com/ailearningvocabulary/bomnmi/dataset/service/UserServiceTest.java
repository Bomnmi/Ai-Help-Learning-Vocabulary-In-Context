package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.User;
import com.ailearningvocabulary.bomnmi.api.service.UserService;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.dataset.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/29 14:59
 */
@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest

public class UserServiceTest {
    @MockBean
    private UserMapper userMapper;

    @Autowired
    private UserService underTest;

    @Value("${ai.config.password-salt}")
    private String passwordSalt;

    @Test
    void testCheckUserExists() {
        String password = CommonUtil.generateUUID();
        User user = new User();

        when(userMapper.selectUserByEmailAndPassword((String) any(), (String) any())).thenReturn(user);
        underTest.queryUserEmailAndPasswordAreCorrect("jane.doe@example.org", password);
        verify(userMapper).selectUserByEmailAndPassword((String) any(), (String) any());
    }

    @Test
    void testQueryUserById(){
        underTest.queryUserById(CommonUtil.generateUUID());
        verify(userMapper).selectUserById(any());
    }



    @Test
    void testUpdateUserLoginTime(){
        when(userMapper.updateUserLastLoginTimeById(any(), any())).thenReturn(1);
        underTest.updateUserLoginTime(CommonUtil.generateUUID());
        verify(userMapper).updateUserLastLoginTimeById(any(), any());
    }

    @Test
    void testCheckEmailIsRegistered(){
        when(userMapper.selectUserByEmail(any())).thenReturn(new User());
        assertTrue(underTest.checkEmailIsRegistered(any()));
        verify(userMapper).selectUserByEmail(any());
    }

    @Test
    void testRegisterNewUser(){
        underTest.registerNewUser("624883617@qq.com", CommonUtil.generateUUID());
        verify(userMapper).insertNewUser(any(User.class));
    }
}
