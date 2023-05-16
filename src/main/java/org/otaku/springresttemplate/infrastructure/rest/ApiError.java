package org.otaku.springresttemplate.infrastructure.rest;

import lombok.Data;

//API异常携带错误码
@Data
public class ApiError {
    private int errorCode;
    private String message;
    private Object detail;

    public ApiError() {}

    public ApiError(int errorCode, String message, Object detail) {
        this.errorCode = errorCode;
        this.message = message;
        this.detail = detail;
    }

    public ApiError(int errorCode, String message) {
        this(errorCode, message, null);
    }

    public static ApiError valueOf(BusinessException e) {
        return new ApiError(e.getErrorCode(), e.getMessage());
    }

}
