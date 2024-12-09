package com.phamtanhoang.profile.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.phamtanhoang.profile.dto.identity.TokenExchangeParam;
import com.phamtanhoang.profile.dto.identity.TokenExchangeResponse;
import com.phamtanhoang.profile.dto.identity.UserCreationParam;

import feign.QueryMap;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {
    @PostMapping(
            value = "/realms/devhoangpham/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

    @PostMapping(value = "/admin/realms/devhoangpham/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token, @RequestBody UserCreationParam userCreationParam);
}
