package com.phamtanhoang.identity_service.controller;


import com.nimbusds.jose.JOSEException;
import com.phamtanhoang.identity_service.dto.request.AuthenticationRequest;
import com.phamtanhoang.identity_service.dto.request.IntrospectRequest;
import com.phamtanhoang.identity_service.dto.request.LogoutRequest;
import com.phamtanhoang.identity_service.dto.request.RefreshRequest;
import com.phamtanhoang.identity_service.dto.response.ApiResponse;
import com.phamtanhoang.identity_service.dto.response.AuthenticationResponse;
import com.phamtanhoang.identity_service.dto.response.IntrospectResponse;
import com.phamtanhoang.identity_service.dto.response.LogoutResponse;
import com.phamtanhoang.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  AuthenticationService authenticationService;

  @PostMapping("/token")
  ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    var result = authenticationService.authenticate(request);
   return ApiResponse.<AuthenticationResponse>builder()
       .result(result)
       .code(1000)
       .build();
  }

  @PostMapping("/introspect")
  ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
    var result = authenticationService.introspect(request);
    return ApiResponse.<IntrospectResponse>builder()
        .result(result)
        .code(1000)
        .build();
  }

  @PostMapping("/logout")
  ApiResponse<LogoutResponse> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
    var result = authenticationService.logout(request);
    return ApiResponse.<LogoutResponse>builder()
        .message(result.getMessage())
        .build();
  }

  @PostMapping("/refresh")
  ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
    var result = authenticationService.refreshToken(request);
    return ApiResponse.<AuthenticationResponse>builder()
        .result(result)
        .code(1000)
        .build();
  }

}
