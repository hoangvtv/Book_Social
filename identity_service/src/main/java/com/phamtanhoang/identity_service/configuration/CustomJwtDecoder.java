package com.phamtanhoang.identity_service.configuration;


import com.nimbusds.jose.JOSEException;
import com.phamtanhoang.identity_service.dto.request.IntrospectRequest;
import com.phamtanhoang.identity_service.exception.ErrorCode;
import com.phamtanhoang.identity_service.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

  private static final Logger log = LoggerFactory.getLogger(CustomJwtDecoder.class);

  @Value("${jwt.signerKey}")
  private String signerKey;

  private final AuthenticationService authenticationService;

  private NimbusJwtDecoder nimbusJwtDecoder = null;

  public CustomJwtDecoder(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public Jwt decode(String token) throws JwtException {
    try {
      var response = authenticationService.introspect(IntrospectRequest.builder()
              .token(token)
              .build());
      if (!response.isValid()) {
        throw new JwtException(ErrorCode.VERIFY_TOKEN_FAILED.getMessage());
      }
    } catch (JOSEException | ParseException e) {
      log.error(e.getMessage());
      throw new JwtException(e.getMessage());
    }

    if (Objects.isNull(nimbusJwtDecoder)) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
      nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
          .macAlgorithm(MacAlgorithm.HS512)
          .build();
    }
    log.info(nimbusJwtDecoder.toString());
    return nimbusJwtDecoder.decode(token);
  }
}


