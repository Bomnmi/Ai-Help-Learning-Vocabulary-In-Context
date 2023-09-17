package com.ailearningvocabulary.bomnmi.common.enums;

import com.ailearningvocabulary.bomnmi.common.interfaces.ResponseInfoInterface;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/30 14:46
 */
public enum ResultCode implements ResponseInfoInterface {

    //Definition of error
    SUCCESS("1000", "request success"),
    SYSTEM_INNER_ERROR("2000", "system inner error"),
    EMAIL_ILLEGAL("3001", "email is illegal"),
    PASSWORD_ILLEGAL("3002", "password is illegal"),
    USER_NOT_EXIST("3003", "user is not exist"),
    WRONG_EMAIL_OR_PASSWORD("3004", "email or password is wrong"),
    USER_REGISTERED("3005","this email has already been registered"),
    VERIFICATION_CODE_WRONG("3006","the verification code is wrong"),
    WORD_ALREADY_ADDED("4000", "this word has already added"),
    GENERATE_PARAGRAPH_TIME_OUT("4001", "generate paragraph time out"),
    BAD_PARAM_ERROR("5000", "parameter format error"),
    TOKEN_INVALID("5001", "token is wrong"),
    TOKEN_EXPIRED("5002", "token is expired"),

    //Definition of User status code
    WAIT_LEARN_USER("1001","this user is waiting to learn new words"),
    WAIT_REVIEW_USER("1002", "this user is waiting to review words");

    /**
     * Error Code
     */
    private final String resultCode;

    /**
     * Error Message
     */
    private final String resultMessage;

    ResultCode(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    @Override
    public String getResultCode() {
        return this.resultCode;
    }

    @Override
    public String getResultMessage() {
        return this.resultMessage;
    }

}
