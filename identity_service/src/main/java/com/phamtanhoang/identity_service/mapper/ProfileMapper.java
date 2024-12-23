package com.phamtanhoang.identity_service.mapper;


import com.phamtanhoang.identity_service.dto.request.ProfileCreationRequest;
import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
  ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
