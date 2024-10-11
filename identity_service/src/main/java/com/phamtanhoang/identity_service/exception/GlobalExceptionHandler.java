package com.phamtanhoang.identity_service.exception;

import com.phamtanhoang.identity_service.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String MIN_ATTRIBUTE = "min";

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
    Map attributes = null;
    try {
      errorCode = ErrorCode.valueOf(enumKey);

      var constrainViolation = e.getBindingResult().getAllErrors().getFirst()
          .unwrap(ConstraintViolation.class);

      attributes = constrainViolation.getConstraintDescriptor().getAttributes();
    } catch (IllegalArgumentException exception) {
      log.error(exception.getMessage());
    }

    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes)
        : errorCode.getMessage());

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

  @ExceptionHandler(JwtException.class)
  ResponseEntity<ApiResponse<Void>> handlingJwtException(JwtException e) {
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

  private String mapAttribute(String message, Map<String, Object> attributes){
    String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
    return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
  }
}
