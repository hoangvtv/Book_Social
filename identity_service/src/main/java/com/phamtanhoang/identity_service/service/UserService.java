package com.phamtanhoang.identity_service.service;

import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.request.UserUpdateRequest;
import com.phamtanhoang.identity_service.dto.response.UserResponse;
import com.phamtanhoang.identity_service.entity.User;
import com.phamtanhoang.identity_service.enums.Role;
import com.phamtanhoang.identity_service.exception.AppException;
import com.phamtanhoang.identity_service.exception.ErrorCode;
import com.phamtanhoang.identity_service.mapper.UserMapper;
import com.phamtanhoang.identity_service.repository.RoleRepository;
import com.phamtanhoang.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.
    PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
  UserRepository userRepository;
  RoleRepository roleRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;

  public UserResponse createUser(UserCreationRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXITSTED);
    }
    User user = userMapper.toUser(request);

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    var roles = roleRepository.findAllById(List.of("USER"));
    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

//using hasRole with role, hasAuthority with permission
//  @PreAuthorize("hasRole('ADMIN')")
  @PreAuthorize("hasAuthority('APPROVE_POST')")
  public List<UserResponse> getUsers() {
    log.info("In method getUsers");
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
  }

  @PostAuthorize("returnObject.username == authentication.name")
  public UserResponse getUser(String userId) {
    log.info("In method getUser");
    return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND)));
  }

  public UserResponse getMyInfo(){
    var context = SecurityContextHolder.getContext();
    var name = context.getAuthentication().getName();
    var user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
    return userMapper.toUserResponse(user);
  }

  public UserResponse updateUser(String userId, UserUpdateRequest request) {
    User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

    userMapper.updateUser(user, request);

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public String deleteUser(String userId) {
    userRepository.deleteById(userId);
    return "User has been deleted";
  }
}
