package com.ailearningvocabulary.bomnmi.web.interceptor;

import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.PrintWriter;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/2 19:02
 */
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //If method is "OPTIONS", return true
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String headerId = request.getHeader("uid");
        String token = request.getHeader("Authorization");
        try{
            if (StringUtils.isNotBlank(token)) {
                //Bearer
                token = token.substring(7);
                //read token
                Claims claims = CommonUtil.readJwt(token);
                //get the data from jwt
                String jwtUid = (String) claims.get("id");

                //if the token's uid and request's uid is same, request can be handled
                return headerId.equals(String.valueOf(jwtUid));
            }
        }catch(Exception e){
            e.printStackTrace();
            //system error.
            ResponseResult result = ResponseResult.error(ResultCode.TOKEN_EXPIRED);
            //use HttpResponse print out
            String responseJson = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(responseJson);
            out.flush();
            out.close();
            return false;
        }
        ResponseResult result = ResponseResult.error(ResultCode.TOKEN_INVALID);
        //use HttpResponse print out
        String responseJson = JSONObject.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
        out.close();
        return false;

    }
}
