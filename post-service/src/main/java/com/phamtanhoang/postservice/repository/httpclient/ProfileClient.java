package com.phamtanhoang.postservice.repository.httpclient;


import com.phamtanhoang.postservice.dto.ApiResponse;
import com.phamtanhoang.postservice.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {

  @GetMapping("/internal/users/{userId}")
  ApiResponse<UserProfileResponse> getUser(@PathVariable("userId") String userId);
}