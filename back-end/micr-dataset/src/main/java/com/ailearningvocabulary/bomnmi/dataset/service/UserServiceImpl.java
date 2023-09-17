package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.User;
import com.ailearningvocabulary.bomnmi.api.service.UserService;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.dataset.mappers.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/29 13:47
 */
@DubboService(interfaceClass = UserService.class, version = "1.0")
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Value("${ai.config.password-salt}")
    private String passwordSalt;

    @Override
    public User queryUserEmailAndPasswordAreCorrect(String email, String password) {
        //Add salt to password and encrypt again using md5
        String newPassword = CommonUtil.encryptSaltedPasswordUsingMd5(password + passwordSalt);

        return userMapper.selectUserByEmailAndPassword(email, newPassword);

    }

    @Override
    public int updateUserLoginTime(String id) {
        //Set today time
        LocalDateTime today = LocalDateTime.now();
        return userMapper.updateUserLastLoginTimeById(id, today);
    }

    @Override
    public boolean checkEmailIsRegistered(String email) {
        return userMapper.selectUserByEmail(email) != null;
    }

    @Override
    public int registerNewUser(String email, String password) {
        User user = new User();
        //add salt and encrypt again
        String newPassword = CommonUtil.encryptSaltedPasswordUsingMd5(password + passwordSalt);
        user.setId(CommonUtil.generateUUID());
        user.setUserName(email);
        user.setEmail(email);
        user.setPassword(newPassword);
        user.setCreateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());

        return userMapper.insertNewUser(user);
    }

    @Override
    public User queryUserById(String id) {
        return userMapper.selectUserById(id);
    }

    @Override
    public int updateSelectiveUser(User user) {
        return userMapper.updateSelectiveUser(user);
    }


}
