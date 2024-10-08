package com.phamtanhoang.identity_service.controller;


import com.phamtanhoang.identity_service.dto.request.RoleRequest;
import com.phamtanhoang.identity_service.dto.response.ApiResponse;
import com.phamtanhoang.identity_service.dto.response.RoleResponse;
import com.phamtanhoang.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {

  RoleService roleService;

  @PostMapping()
  ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
    return ApiResponse.<RoleResponse>builder()
        .result(roleService.create(request))
        .build();
  }

  @GetMapping()
  ApiResponse<List<RoleResponse>> getAll() {
    return ApiResponse.<List<RoleResponse>>builder()
        .result(roleService.getAll())
        .build();
  }

  @DeleteMapping("/{role}")
  ApiResponse<String> delete(@PathVariable String role) {
    return ApiResponse.<String>builder()
        .result(roleService.delete(role))
        .build();
  }

}