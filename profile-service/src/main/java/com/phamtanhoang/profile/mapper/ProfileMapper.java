package com.phamtanhoang.profile.mapper;

import org.mapstruct.Mapper;

import com.phamtanhoang.profile.dto.request.RegistrationRequest;
import com.phamtanhoang.profile.dto.response.ProfileResponse;
import com.phamtanhoang.profile.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(RegistrationRequest request);

    ProfileResponse toProfileResponse(Profile profile);
}
