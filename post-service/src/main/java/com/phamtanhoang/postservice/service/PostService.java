package com.phamtanhoang.postservice.service;

import com.phamtanhoang.postservice.dto.request.PostRequest;
import com.phamtanhoang.postservice.dto.response.PostResponse;
import com.phamtanhoang.postservice.entity.Post;
import com.phamtanhoang.postservice.mapper.PostMapper;
import com.phamtanhoang.postservice.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {

  PostRepository postRepository;
  PostMapper postMapper;

  public PostResponse createPost(PostRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Post post = Post.builder()
        .userId(authentication.getName())
        .content(request.getContent())
        .createdDate(Instant.now())
        .modifiedDate(Instant.now())
        .build();

    return postMapper.toPostResponse(postRepository.save(post));
  }

  public List<PostResponse> getMyPosts(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    return postRepository.findAllByUserId(userId)
        .stream()
        .map(postMapper::toPostResponse)
        .toList();
  }
}
