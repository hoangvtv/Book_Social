package com.phamtanhoang.identity_service.configuration;

import com.phamtanhoang.identity_service.exception.AppException;
import com.phamtanhoang.identity_service.exception.ErrorCode;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Component
@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {

  private static final String AUTHORIZATION_HEADER = "Authorization";

  @Override
  public void apply(RequestTemplate requestTemplate) {
    String authHeader = getAuthorizationHeader();

    if (StringUtils.hasText(authHeader)) {
      log.debug("Adding Authorization header to the request");
      requestTemplate.header(AUTHORIZATION_HEADER, authHeader);
    } else {
      log.warn("No Authorization header found in the incoming request");
    }
  }

  private String getAuthorizationHeader() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes == null) {
      log.error("No request context available. Unable to retrieve Authorization header.");
      throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    return attributes.getRequest().getHeader(AUTHORIZATION_HEADER);
  }
}
