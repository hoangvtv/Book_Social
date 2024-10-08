package com.phamtanhoang.identity_service.mapper;

import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.request.UserUpdateRequest;
import com.phamtanhoang.identity_service.dto.response.UserResponse;
import com.phamtanhoang.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreationRequest request);

  //  @Mapping(source = "", target = "") // when 2 properties different
  //  @Mapping( target = "", ignore = true) // ignore properties
  UserResponse toUserResponse(User user);

  @Mapping(target = "roles", ignore = true)
  void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
