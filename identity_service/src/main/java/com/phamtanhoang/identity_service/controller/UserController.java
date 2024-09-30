package com.phamtanhoang.identity_service.controller;


import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.request.UserUpdateRequest;
import com.phamtanhoang.identity_service.dto.response.ApiResponse;
import com.phamtanhoang.identity_service.entity.User;
import com.phamtanhoang.identity_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping()
  ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
    ApiResponse<User> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.createUser(request));

    return apiResponse;
  }

  @GetMapping
  ApiResponse<List<User>> getUsers() {
    ApiResponse<List<User>> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.getUsers());

    return apiResponse;
  }

  @GetMapping("/{userId}")
  ApiResponse<User> getUser(@PathVariable String userId) {
    ApiResponse<User> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.getUser(userId));

    return apiResponse;
  }

  @PutMapping("/{userId}")
  ApiResponse<User> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
    ApiResponse<User> apiResponse = new ApiResponse<>();
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
