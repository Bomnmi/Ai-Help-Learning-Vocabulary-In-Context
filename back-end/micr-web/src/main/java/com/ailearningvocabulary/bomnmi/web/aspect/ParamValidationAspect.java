package com.ailearningvocabulary.bomnmi.web.aspect;

import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.web.service.EmailService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/30 13:51
 */
@Component
@Aspect
public class ParamValidationAspect {
    @Autowired
    EmailService emailService;

    @Pointcut("execution(public * com.ailearningvocabulary.bomnmi.web.controller.UserController.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object validateEmailOrPassword(ProceedingJoinPoint joinPoint) throws Throwable {
        String[] paraNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < paraNames.length; i++) {
            String paraName = paraNames[i].toLowerCase();
            Object arg = args[i];

            if (paraName.contains("email") && arg instanceof String) {
                if (!CommonUtil.verifyEmailFormat((String) arg)) {
                    throw new ProjectException(ResultCode.EMAIL_ILLEGAL);
                }
            }else if (paraName.contains("password") && arg instanceof String) {
                if (!CommonUtil.verifyPasswordFormat((String) arg)) {
                    throw new ProjectException(ResultCode.PASSWORD_ILLEGAL);
                }
            }
        }
        return joinPoint.proceed();
    }

//    @Around("controllerMethods() && args(email, password,..)")
//    public Object validateLogin(ProceedingJoinPoint joinPoint, String email, String password) throws Throwable {
//        if (!CommonUtil.verifyEmailFormat(email)) {
//            throw new ProjectException(ResultCode.EMAIL_ILLEGAL);
//        }
//        if (!CommonUtil.verifyPasswordFormat(password)) {
//            throw new ProjectException(ResultCode.PASSWORD_ILLEGAL);
//        }
//        return joinPoint.proceed();
//    }

}
