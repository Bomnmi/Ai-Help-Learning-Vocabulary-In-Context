package com.ailearningvocabulary.bomnmi.common.exception;

import com.ailearningvocabulary.bomnmi.common.interfaces.ResponseInfoInterface;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/30 14:43
 */
public class ProjectException extends RuntimeException{

    /**
     * Error Code
     */
    protected String errorCode;

    /**
     * Error Message
     */
    protected String errorMessage;

    public ProjectException() {
        super();
    }

    public ProjectException(ResponseInfoInterface responseInfoInterface) {
        super(responseInfoInterface.getResultCode());
        this.errorCode = responseInfoInterface.getResultCode();
        this.errorMessage = responseInfoInterface.getResultMessage();
    }

    public ProjectException(ResponseInfoInterface responseInfoInterface, Throwable cause) {
        super(responseInfoInterface.getResultCode(),cause);
        this.errorCode = responseInfoInterface.getResultCode();
        this.errorMessage = responseInfoInterface.getResultMessage();
    }

    public ProjectException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public ProjectException(String errorCode, String errorMessage) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ProjectException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
