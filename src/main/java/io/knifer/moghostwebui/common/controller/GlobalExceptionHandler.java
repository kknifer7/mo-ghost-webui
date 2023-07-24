package io.knifer.moghostwebui.common.controller;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.util.ServletUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 全局异常处理
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理参数异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class,
            TypeMismatchException.class
    })
    public ApiResult<Void> handle(Exception e){
        log.warn(e.getMessage(), e);

        return ApiResult.fail(ErrorCodes.VALIDATION_FAILED);
    }

    /**
     * 处理请求方式错误异常
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<Void> handle(HttpRequestMethodNotSupportedException e){
        log.warn(e.getMessage(), e);

        return ApiResult.fail(ErrorCodes.VALIDATION_METHOD_NOT_ALLOWED);
    }

    /**
     * 处理业务校验异常
     */
    @ExceptionHandler(MoException.class)
    public ApiResult<Void> handle(MoException e){
        Integer statusValue = e.getCode();
        HttpStatus httpStatus;

        log.warn(e.getMessage(), e);
        if (statusValue < 1000){
            httpStatus = HttpStatus.resolve(statusValue);
            ServletUtil.setResponseStatus(httpStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus);
        } else if (statusValue > 4000 && statusValue < 5000){
            ServletUtil.setResponseStatus(HttpStatus.BAD_REQUEST);
        }

        return ApiResult.fail(statusValue);
    }

    /**
     * 处理数据库字段不存在异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PropertyReferenceException.class)
    public ApiResult<Void> handle(PropertyReferenceException e){
        log.warn(e.getMessage(), e);

        return ApiResult.fail(ErrorCodes.VALIDATION_FAILED);
    }

    /**
     * 处理其他异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handleOthers(Exception e){
        log.error(e.getMessage(), e);

        return ApiResult.fail(ErrorCodes.UNKNOWN);
    }
}
