package com.phamtanhoang.identity_service.controller;


import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.request.UserUpdateRequest;
import com.phamtanhoang.identity_service.dto.response.ApiResponse;
import com.phamtanhoang.identity_service.dto.response.UserResponse;
import com.phamtanhoang.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
  UserService userService;

  @PostMapping()
  ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.createUser(request));

    return apiResponse;
  }

  @GetMapping
  ApiResponse<List<UserResponse>> getUsers() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("Username: {}", authentication.getName());
    authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
    ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.getUsers());

    return apiResponse;
  }

  @GetMapping("/{userId}")
  ApiResponse<UserResponse> getUser(@PathVariable String userId) {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.getUser(userId));

    return apiResponse;
  }

  @GetMapping("/my-info")
  ApiResponse<UserResponse> getMyInfo() {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.getMyInfo());

    return apiResponse;
  }

  @PutMapping("/{userId}")
  ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.updateUser(userId, request));

    return apiResponse;
  }

  @DeleteMapping("/{userId}")
  ApiResponse<String> deleteUser(@PathVariable String userId) {
    ApiResponse<String> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.deleteUser(userId));

    return apiResponse;
  }
}
