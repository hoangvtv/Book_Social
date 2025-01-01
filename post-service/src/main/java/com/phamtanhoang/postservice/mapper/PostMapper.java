package com.phamtanhoang.postservice.mapper;


import com.phamtanhoang.postservice.dto.request.PostRequest;
import com.phamtanhoang.postservice.dto.response.PostResponse;
import com.phamtanhoang.postservice.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
  PostResponse toPostResponse(Post post);
  Post toPost(PostRequest postRequest);
}
