package com.phamtanhoang.identity_service.exception;

public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(9999, "UNCATEGORIZED ERROR"),
  INVALID_KEY(1001, "INVALID MESSAGE KEY"),
  USER_EXITSTED(1002, "User existed!"),
  USERNAME_INVALID(1003, "Username must be at least 3 characters"),
  PASSWORD_INVALID(1004, "Password must be at least 8 characters"),
  USER_NOTFOUND(1005, "User not found!"),
  ;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  private int  code;
  private String message;

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

}
