package com.ailearningvocabulary.bomnmi.web.service;

import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;

public interface TokenService {

    <T> ResponseResult setTokenIntoResponseResult(T t);
}
