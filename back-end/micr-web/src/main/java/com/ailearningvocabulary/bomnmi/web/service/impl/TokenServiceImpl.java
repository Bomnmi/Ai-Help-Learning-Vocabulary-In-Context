package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.common.utils.ReflectUtil;
import com.ailearningvocabulary.bomnmi.web.service.TokenService;
import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/3 22:36
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public <T> ResponseResult setTokenIntoResponseResult(T t) {
        try {
            String jwt = CommonUtil.generateJwt(t, 60*24*7);
            //Successfully log in, pass user to front-end
            ResponseResult responseResult = ResponseResult.success(ReflectUtil.parseObjectToMap(t));
            responseResult.setAccessToken(jwt);
            return responseResult;
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }
}
