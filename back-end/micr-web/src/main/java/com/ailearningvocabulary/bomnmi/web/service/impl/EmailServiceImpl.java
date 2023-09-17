package com.ailearningvocabulary.bomnmi.web.service.impl;


import com.ailearningvocabulary.bomnmi.common.constants.RedisConstant;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.web.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/29 23:22
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendVerificationCode(String toEmail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Verification Code");
        String verificationCode = CommonUtil.getNDigitsRandomNumber(4);
        mailMessage.setText(verificationCode);
        //Add code into redis(15min expiration time)
        stringRedisTemplate.boundValueOps(RedisConstant.KEY_EMAIL_CODE + toEmail)
                .set(verificationCode, RedisConstant.CODE_EXPIRE_TIME, RedisConstant.TIME_UNIT);

        javaMailSender.send(mailMessage);
    }

    @Override
    public boolean checkVerificationCode(String toEmail, String verificationCode) {
        String key = RedisConstant.KEY_EMAIL_CODE;
        if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey(key + toEmail))) {
            return false;
        }
        // check whether the user's code equal to the sent code
        return verificationCode.equals(
                stringRedisTemplate.boundValueOps(key + toEmail)
                        .get()
        );
    }
}
