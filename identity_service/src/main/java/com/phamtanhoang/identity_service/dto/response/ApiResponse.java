package com.phamtanhoang.identity_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) //when properties is null, it doesn't show in api response
public class ApiResponse <T> {
  int code = 1000;
  String message;
  T result;

}
