package com.phamtanhoang.identity_service.exception;

import com.phamtanhoang.identity_service.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  ResponseEntity<ApiResponse<Void>> handlingRuntimeException(RuntimeException e) {
    ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());

    return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(AppException.class)
  ResponseEntity<ApiResponse<Void>> handlingRuntimeException(AppException e) {
    ErrorCode errorCode = e.getErrorCode();

    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());

    return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiResponse<Void>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
    ErrorCode errorCode = ErrorCode.INVALID_KEY;

    try {
      errorCode = ErrorCode.valueOf(enumKey);
    } catch (IllegalArgumentException exception) {
      log.error(exception.getMessage());
    }

    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());

    return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(ParseException.class)
  ResponseEntity<ApiResponse<Void>> handlingEOFException(ParseException e) {
    ErrorCode errorCode = ErrorCode.VERIFY_TOKEN_FAILED;

    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());

    return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<ApiResponse<Void>> handlingAccessDeniedException(AccessDeniedException e) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    return ResponseEntity.status(errorCode.getHttpStatusCode()).body(
        ApiResponse.<Void>builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build()
    );
  }

}
