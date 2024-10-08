package com.phamtanhoang.identity_service.service;


import com.phamtanhoang.identity_service.dto.request.RoleRequest;
import com.phamtanhoang.identity_service.dto.response.PermissionResponse;
import com.phamtanhoang.identity_service.dto.response.RoleResponse;
import com.phamtanhoang.identity_service.entity.Role;
import com.phamtanhoang.identity_service.mapper.RoleMapper;
import com.phamtanhoang.identity_service.repository.PermissionRepository;
import com.phamtanhoang.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
  RoleRepository roleRepository;
  RoleMapper roleMapper;
  PermissionRepository permissionRepository;

  public RoleResponse create(RoleRequest request){
    Role role = roleMapper.toRole(request);

    var permissions =  permissionRepository.findAllById(request.getPermissions());
    role.setPermissions(new HashSet<>(permissions));

    return roleMapper.toRoleResponse(roleRepository.save(role));
  }

  public List<RoleResponse> getAll(){
    var roles = roleRepository.findAll();
    return roles.stream().map(roleMapper::toRoleResponse).toList();
  }

  public String delete(String roleName){
    roleRepository.deleteById(roleName);
    return "Role has been deleted";
  }

}
