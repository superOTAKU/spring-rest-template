package org.otaku.springresttemplate.infrastructure.rest;

public interface FrameworkErrorCodeProvider {

    //框架内异常，统一1个错误码
    int getFrameworkErrorCode();

    //未知异常统一1个错误码
    int getUnknownErrorCode();

}
