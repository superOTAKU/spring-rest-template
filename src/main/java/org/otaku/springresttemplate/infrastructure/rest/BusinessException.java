package org.otaku.springresttemplate.infrastructure.rest;

public class BusinessException extends RuntimeException {
    private final int errorCode;

    public BusinessException(int errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
