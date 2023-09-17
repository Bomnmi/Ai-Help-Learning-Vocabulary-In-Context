package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.User;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper underTest;

    private String id = CommonUtil.generateUUID();
    private String password = CommonUtil.generateUUID();
    private String email = "bomnmi@gmail.com";

    @Test
    void testInsert(){
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());

        int test = underTest.insert(user);
        User user1 = underTest.selectUserByEmailAndPassword(email, password);
        System.out.println(user1);
        assertThat(test).isEqualTo(1);
    }

    @Test
    void testSelectUserById(){
        User user = new User();
        String id = CommonUtil.generateUUID();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());
        underTest.insert(user);

        User test = underTest.selectUserById(id);
        assertThat(test.getId()).isEqualTo(id);
    }

    @Test
    void testSelectUserByEmailAndPassword(){
        String id = CommonUtil.generateUUID();
        String email = "haha123@gmail.com";
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);

        underTest.insert(user);

        User test = underTest.selectUserByEmailAndPassword(email, password);

        assertThat(test.getEmail()).isEqualTo(user.getEmail());
        assertThat(test.getPassword()).isEqualTo(user.getPassword());
        assertThat(test.getId()).isEqualTo(user.getId());
    }

    @Test
    void testUpdateUserLastLoginTimeById(){
        String id = CommonUtil.generateUUID();
        String email = "hahapipi@gmail.com";
        LocalDateTime today = LocalDateTime.now();

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);

        underTest.insert(user);
        user.setLastLoginTime(today);

        int test = underTest.updateUserLastLoginTimeById(id, today);

        assertThat(test).isEqualTo(1);
    }

    @Test
    void testSelectUserByEmail(){
        String id = CommonUtil.generateUUID();
        String email = "hahapip@gmail.com";
        LocalDateTime today = LocalDateTime.now();

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);

        underTest.insert(user);
        User test = underTest.selectUserByEmail(email);
        assertThat(test.getEmail()).isEqualTo(email);
    }

    @Test
    void testInsertNewUser(){
        String id = CommonUtil.generateUUID();
        String email = "hahap@gmail.com";
        LocalDateTime today = LocalDateTime.now();
        User user = new User();
        user.setId(id);
        user.setUserName(email);
        user.setEmail(email);
        user.setPassword(password);
        user.setLastLoginTime(today);
        user.setCreateTime(today);

        underTest.insertNewUser(user);
        User test = underTest.selectUserByEmail(email);
        assertThat(test).isEqualTo(user);
    }

    @Test
    void testUpdateSelectiveUser(){
        String id = CommonUtil.generateUUID();
        String email = "hahap@gmail.com";
        LocalDateTime today = LocalDateTime.now();
        User user = new User();
        user.setId(id);
        user.setUserName(email);
        user.setEmail(email);
        user.setPassword(password);
        user.setLastLoginTime(today);
        user.setCreateTime(today);

        underTest.insertNewUser(user);

        user.setUserName("bomnmi");
        int rows = underTest.updateSelectiveUser(user);
        User test = this.underTest.selectUserById(user.getId());
        assertThat(test.getUserName()).isEqualTo(user.getUserName());
        assertThat(rows).isEqualTo(1);
    }

}