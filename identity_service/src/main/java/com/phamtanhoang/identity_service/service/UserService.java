package com.phamtanhoang.identity_service.service;

import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.request.UserUpdateRequest;
import com.phamtanhoang.identity_service.dto.response.UserResponse;
import com.phamtanhoang.identity_service.entity.User;
import com.phamtanhoang.identity_service.exception.AppException;
import com.phamtanhoang.identity_service.exception.ErrorCode;
import com.phamtanhoang.identity_service.mapper.UserMapper;
import com.phamtanhoang.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
  UserRepository userRepository;
  UserMapper userMapper;

  public User createUser(UserCreationRequest request) {
    if(userRepository.existsByUsername(request.getUsername())) {
        throw new AppException(ErrorCode.USER_EXITSTED);
    }
    User user = userMapper.toUser(request);

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public UserResponse getUser(String userId) {
    return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND)));
  }

  public UserResponse updateUser(String userId, UserUpdateRequest request) {
    User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

    userMapper.updateUser(user, request);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public String deleteUser(String userId) {
    userRepository.deleteById(userId);
    return "User has been deleted";
  }
}
