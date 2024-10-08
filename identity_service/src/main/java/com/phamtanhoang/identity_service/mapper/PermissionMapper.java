package com.phamtanhoang.identity_service.mapper;


import com.phamtanhoang.identity_service.dto.request.PermissionRequest;
import com.phamtanhoang.identity_service.dto.response.PermissionResponse;
import com.phamtanhoang.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest request);
  PermissionResponse toPermissionResponse(Permission permission);

}
