package com.phamtanhoang.postservice.controller;

import com.phamtanhoang.postservice.dto.ApiResponse;
import com.phamtanhoang.postservice.dto.PageResponse;
import com.phamtanhoang.postservice.dto.request.PostRequest;
import com.phamtanhoang.postservice.dto.response.PostResponse;
import com.phamtanhoang.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
  PostService postService;

  @PostMapping("/create")
  public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
    return ApiResponse.<PostResponse>builder()
        .result(postService.createPost(request))
        .build();
  }

  @GetMapping("/my-posts")
  ApiResponse<PageResponse<PostResponse>> myPosts(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size
      ){
    return ApiResponse.<PageResponse<PostResponse>>builder()
        .result(postService.getMyPosts(page, size))
        .build();
  }

}