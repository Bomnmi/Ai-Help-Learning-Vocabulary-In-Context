package com.ailearningvocabulary.bomnmi.web.service;

public interface EmailService {

    void sendVerificationCode(String toEmail);

    boolean checkVerificationCode(String toEmail, String verificationCode);

}
