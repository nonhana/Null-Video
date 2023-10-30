package com.typeofNull.nullvideo.exception;

import com.typeofNull.nullvideo.common.BaseResponse;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Andy
 * @data 2023/10/25
 * @Description
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public BaseResponse<?> maxUploadSizeExceededExceptionHandler(MultipartException e) {
        log.error("video is too big",e);
       return ResultUtils.error(ErrorCode.FILE_UPLOAD_ERROR,e.getMessage());
    }
}
