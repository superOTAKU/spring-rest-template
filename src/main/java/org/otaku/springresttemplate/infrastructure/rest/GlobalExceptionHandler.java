package org.otaku.springresttemplate.infrastructure.rest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;
import java.util.stream.Collectors;

public abstract class GlobalExceptionHandler {

    private static final Map<Class<? extends Exception>, HttpStatus> exceptionStatusMap = new HashMap<>();

    static {
        exceptionStatusMap.put(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);
        exceptionStatusMap.put(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        exceptionStatusMap.put(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
        exceptionStatusMap.put(MissingPathVariableException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionStatusMap.put(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(ServletRequestBindingException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(ConversionNotSupportedException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionStatusMap.put(TypeMismatchException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionStatusMap.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(BindException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(NoHandlerFoundException.class, HttpStatus.NOT_FOUND);
        exceptionStatusMap.put(AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> exception(Exception e) {
        HttpStatus httpStatus = exceptionStatusMap.get(e.getClass());
        if (httpStatus == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(getUnknownErrorCode(), "unknown error"));
        }
        return ResponseEntity.status(httpStatus).body(new ApiError(getFrameworkErrorCode(),
                "framework error: %s".formatted(e.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        //返回json描述每个字段的错误
        List<FieldErrorDetail> fieldErrors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(fe ->
                fieldErrors.add(new FieldErrorDetail(fe.getField(), fe.getDefaultMessage())));
        return new ResponseEntity<>(
                new ApiError(getFrameworkErrorCode(), "param error", fieldErrors),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handle(ConstraintViolationException exs) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
        for (ConstraintViolation<?> item : violations) {
            stringBuilder.append(item.getMessage()).append(" ");
        }
        return new ResponseEntity<>(
                new ApiError(getFrameworkErrorCode(), stringBuilder.toString(), stringBuilder),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理入参时，参数校验异常
     *
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleException(BindException e) {
        // 获取错误信息
        String msg = e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("，"));
        return new ResponseEntity<>(
                new ApiError(getFrameworkErrorCode(), "request param format error, see detail.", msg),
                HttpStatus.BAD_REQUEST);
    }

    protected abstract int getFrameworkErrorCode();

    protected abstract int getUnknownErrorCode();

}
