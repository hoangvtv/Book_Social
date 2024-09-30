package com.phamtanhoang.identity_service.service;

import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.request.UserUpdateRequest;
import com.phamtanhoang.identity_service.entity.User;
import com.phamtanhoang.identity_service.exception.AppException;
import com.phamtanhoang.identity_service.exception.ErrorCode;
import com.phamtanhoang.identity_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(UserCreationRequest request) {
    if(userRepository.existsByUsername(request.getUsername())) {
        throw new AppException(ErrorCode.USER_EXITSTED);
    }
    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setDob(request.getDob());

    return userRepository.save(user);
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public User getUser(String userId) {
    return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
  }

  public User updateUser(String userId, UserUpdateRequest request) {
    User user = getUser(userId);
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setDob(request.getDob());
    user.setPassword(request.getPassword());

    return userRepository.save(user);
  }

  public String deleteUser(String userId) {
    userRepository.deleteById(userId);
    return "User has been deleted";
  }
}
