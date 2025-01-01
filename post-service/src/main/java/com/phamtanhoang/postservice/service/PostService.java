package com.phamtanhoang.postservice.service;

import com.phamtanhoang.postservice.dto.PageResponse;
import com.phamtanhoang.postservice.dto.request.PostRequest;
import com.phamtanhoang.postservice.dto.response.PostResponse;
import com.phamtanhoang.postservice.entity.Post;
import com.phamtanhoang.postservice.mapper.PostMapper;
import com.phamtanhoang.postservice.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {

  PostRepository postRepository;
  PostMapper postMapper;
  DateTimeFormatter dateTimeFormatter;

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

  public PageResponse<PostResponse> getMyPosts(int page, int size){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Sort sort = Sort.by( "createdDate").descending();
    Pageable pageable = PageRequest.of(page -1, size, sort);
    var pageData = postRepository.findAllByUserId(userId, pageable);

    var postList = pageData.getContent().stream().map(post -> {
      var postResponse = postMapper.toPostResponse(post);
      postResponse.setCreatedAt(dateTimeFormatter.formatDate(post.getCreatedDate()));
      return postResponse;
    }).toList();

    return PageResponse.<PostResponse>builder()
        .currentPage(page)
        .pageSize(pageData.getSize())
        .totalPages(pageData.getTotalPages())
        .totalElements(pageData.getTotalElements())
        .data(postList)
        .build();
  }
}
