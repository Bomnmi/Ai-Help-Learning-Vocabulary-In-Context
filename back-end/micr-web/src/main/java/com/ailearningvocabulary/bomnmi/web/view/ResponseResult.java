package com.ailearningvocabulary.bomnmi.web.view;

import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.interfaces.ResponseInfoInterface;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/30 15:02
 */
public class ResponseResult {

    /**
     * Response Code
     */
    private String code;

    /**
     * Response Message
     */
    private String message;

    /**
     * Response Data
     */
    private Object data;

    /**
     * Access Token
     */
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ResponseResult() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseResult(ResponseInfoInterface responseInfoInterface) {
        this.code = responseInfoInterface.getResultCode();
        this.message = responseInfoInterface.getResultMessage();
    }

    /**
     * Success
     * @return ResponseResult
     */
    public static ResponseResult success(){
        return new ResponseResult(ResultCode.SUCCESS);
    }

    /**
     * Success with data
     * @param data pass data in
     * @return ResponseResult
     */
    public static ResponseResult success(Object data){
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);
        responseResult.setData(data);
        return responseResult;
    }

    public static ResponseResult error(ResponseInfoInterface responseInfoInterface) {
        return new ResponseResult(responseInfoInterface);
    }

    public static ResponseResult error(String code, String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMessage(message);

        return responseResult;
    }
    public static ResponseResult error(String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode("-1");
        responseResult.setMessage(message);

        return responseResult;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
