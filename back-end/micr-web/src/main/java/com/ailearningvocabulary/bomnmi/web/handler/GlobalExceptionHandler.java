package com.ailearningvocabulary.bomnmi.web.handler;

import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bomnmi
 * @version 1.0
 * @description: Handle the customized exception
 * @date 2023/7/30 15:46
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle the project exception
     *
     * @param request request
     * @param e       project exception
     * @return return json form data to front-end
     */
    @ExceptionHandler(value = ProjectException.class)
    @ResponseBody
    public ResponseResult projectExceptionHandler(HttpServletRequest request, ProjectException e) {
        logger.error("There is a service error, reason:{}", e.getErrorMessage());
        return ResponseResult.error(e.getErrorCode(), e.getErrorMessage());
    }

//    @ExceptionHandler(value = Exception.class)
//    public ResponseResult otherExceptionHandler(HttpServletRequest request, Exception e) {
//        logger.error("There is a service error, reason:{}", e.getMessage());
//        return ResponseResult.error("5000", e.getMessage());
//    }

}
