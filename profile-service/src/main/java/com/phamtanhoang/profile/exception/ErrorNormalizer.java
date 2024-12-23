package com.phamtanhoang.profile.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phamtanhoang.profile.dto.identity.KeyCloakErrorResponse;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorNormalizer {
    private final ObjectMapper objectMapper;
    private final Map<String, ErrorCode> errorCodeMap;

    public ErrorNormalizer() {
        objectMapper = new ObjectMapper();
        errorCodeMap = new HashMap<>();

        errorCodeMap.put("User exists with same username", ErrorCode.USER_EXISTED);
        errorCodeMap.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
        errorCodeMap.put("User name is missing", ErrorCode.USERNAME_IS_MISSING);
    }

    public AppException handleKeyCloakException(FeignException ex) {
        try {
            log.warn("Cannot complete request", ex);
            var response = objectMapper.readValue(ex.contentUTF8(), KeyCloakErrorResponse.class);

            if (Objects.nonNull(response.getErrorMessage()) && errorCodeMap.containsKey(response.getErrorMessage())) {
                return new AppException(errorCodeMap.get(response.getErrorMessage()));
            }
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize content: ", e.getMessage());
        }

        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
