package com.phamtanhoang.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(9999, "UNCATEGORIZED ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_KEY(1001, "INVALID MESSAGE KEY", HttpStatus.BAD_REQUEST),
  USER_EXITSTED(1002, "User existed!", HttpStatus.BAD_REQUEST),
  USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
  PASSWORD_INVALID(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
  USER_NOTFOUND(1005, "User not found!", HttpStatus.NOT_FOUND),
  UNAUTHENTICATED(1006, "UNAUTHENTICATED!", HttpStatus.UNAUTHORIZED),
  VERIFY_TOKEN_FAILED(1007, "VERIFY TOKEN FAILED!", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1008, "You do not have permission!", HttpStatus.FORBIDDEN),
  INVALID_DOB(1009, "You must be over {min} years old", HttpStatus.BAD_REQUEST),
  ;

  ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
    this.code = code;
    this.message = message;
    this.httpStatusCode = httpStatusCode;
  }

  private int  code;
  private String message;
  private HttpStatusCode httpStatusCode;

}
