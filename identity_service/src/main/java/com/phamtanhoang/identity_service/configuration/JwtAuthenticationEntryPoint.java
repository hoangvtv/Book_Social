package com.phamtanhoang.identity_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phamtanhoang.identity_service.dto.response.ApiResponse;
import com.phamtanhoang.identity_service.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException {
    ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

    response.setStatus(errorCode.getHttpStatusCode().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ApiResponse<?> apiResponse = ApiResponse.builder()
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .build();

    ObjectMapper mapper = new ObjectMapper();

    response.getWriter().write(mapper.writeValueAsString(apiResponse));
    response.flushBuffer();
  }
}
