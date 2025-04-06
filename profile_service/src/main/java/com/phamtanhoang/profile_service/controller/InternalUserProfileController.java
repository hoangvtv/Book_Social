package com.phamtanhoang.profile_service.controller;

import org.springframework.web.bind.annotation.*;

import com.phamtanhoang.profile_service.dto.ApiResponse;
import com.phamtanhoang.profile_service.dto.request.ProfileCreationRequest;
import com.phamtanhoang.profile_service.dto.response.UserProfileResponse;
import com.phamtanhoang.profile_service.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping
    UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/{userId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String userId) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfileByUserId(userId))
                .build();
    }
}
