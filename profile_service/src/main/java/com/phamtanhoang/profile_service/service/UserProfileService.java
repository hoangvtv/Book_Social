package com.phamtanhoang.profile_service.service;


import com.phamtanhoang.profile_service.dto.request.ProfileCreationRequest;
import com.phamtanhoang.profile_service.dto.response.UserProfileResponse;
import com.phamtanhoang.profile_service.entity.UserProfile;
import com.phamtanhoang.profile_service.mapper.UserProfileMapper;
import com.phamtanhoang.profile_service.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
  UserProfileRepository userProfileRepository;
  UserProfileMapper userProfileMapper;

  public UserProfileResponse createProfile(ProfileCreationRequest request) {
    UserProfile userProfile = userProfileMapper.toUserProfile(request);
    userProfile = userProfileRepository.save(userProfile);

    return userProfileMapper.toUserProfileResponse(userProfile);
  }

  public UserProfileResponse getProfile(String id) {
    UserProfile userProfile =
        userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));

    return userProfileMapper.toUserProfileResponse(userProfile);
  }
}
