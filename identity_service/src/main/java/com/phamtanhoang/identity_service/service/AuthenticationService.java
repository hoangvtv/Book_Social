package com.phamtanhoang.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.phamtanhoang.identity_service.dto.request.AuthenticationRequest;
import com.phamtanhoang.identity_service.dto.request.IntrospectRequest;
import com.phamtanhoang.identity_service.dto.response.AuthenticationResponse;
import com.phamtanhoang.identity_service.dto.response.IntrospectResponse;
import com.phamtanhoang.identity_service.exception.AppException;
import com.phamtanhoang.identity_service.exception.ErrorCode;
import com.phamtanhoang.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
  private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
  UserRepository userRepository;

  @NonFinal
  @Value("${jwt.signerKey}")
  protected String SINGER_KEY;

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!authenticated) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    var token = generateToken(request.getUsername());
    return AuthenticationResponse.builder()
        .token(token)
        .authenticated(true)
        .build();
  }

  private String generateToken(String username) {
    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

    // build payload with claim
    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(username) //represent user login
        .issuer("phamtanhoang.com") //Identify who issued the token
        .issueTime(new Date())
        .expirationTime(new Date(
            Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() // Token expires in 1 hour
        ))
        .claim("userId", "Custom Claim") //add claim to do something
        .build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(jwsHeader, payload);

    try {
      jwsObject.sign(new MACSigner(SINGER_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Cannot create token", e);
      throw new RuntimeException();
    }
  }

  //verify token
  public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
    var token = request.getToken();

    JWSVerifier jwsVerifier = new MACVerifier(SINGER_KEY.getBytes());

    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    var verify = signedJWT.verify(jwsVerifier);

    return IntrospectResponse.builder()
        .valid(verify && expiryTime.after(new Date()))
        .build();
  }
}
