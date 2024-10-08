package com.phamtanhoang.identity_service.service;


import com.phamtanhoang.identity_service.dto.request.PermissionRequest;
import com.phamtanhoang.identity_service.dto.response.PermissionResponse;
import com.phamtanhoang.identity_service.entity.Permission;
import com.phamtanhoang.identity_service.mapper.PermissionMapper;
import com.phamtanhoang.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
  PermissionRepository permissionRepository;
  PermissionMapper permissionMapper;

  public PermissionResponse create(PermissionRequest request){
    Permission permission = permissionMapper.toPermission(request);
    permissionRepository.save(permission);
    return permissionMapper.toPermissionResponse(permission);
  }

  public List<PermissionResponse> getAll(){
    var permissions = permissionRepository.findAll();
    return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
  }

  public String delete(String permissionName){
    permissionRepository.deleteById(permissionName);
    return "Permission has been deleted";
  }

}
