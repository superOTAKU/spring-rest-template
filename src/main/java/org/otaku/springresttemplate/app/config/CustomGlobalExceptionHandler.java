package org.otaku.springresttemplate.app.config;

import org.otaku.springresttemplate.app.rest.ErrorCodes;
import org.otaku.springresttemplate.infrastructure.rest.GlobalExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends GlobalExceptionHandler{

    @Override
    protected int getFrameworkErrorCode() {
        return ErrorCodes.FRAMEWORK_ERROR;
    }

    @Override
    protected int getUnknownErrorCode() {
        return ErrorCodes.UNKNOWN_ERROR;
    }
}
