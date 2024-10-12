package com.phamtanhoang.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.phamtanhoang.identity_service.dto.request.AuthenticationRequest;
import com.phamtanhoang.identity_service.dto.request.IntrospectRequest;
import com.phamtanhoang.identity_service.dto.request.LogoutRequest;
import com.phamtanhoang.identity_service.dto.request.RefreshRequest;
import com.phamtanhoang.identity_service.dto.response.AuthenticationResponse;
import com.phamtanhoang.identity_service.dto.response.IntrospectResponse;
import com.phamtanhoang.identity_service.dto.response.LogoutResponse;
import com.phamtanhoang.identity_service.entity.InvalidatedToken;
import com.phamtanhoang.identity_service.entity.Permission;
import com.phamtanhoang.identity_service.entity.User;
import com.phamtanhoang.identity_service.exception.AppException;
import com.phamtanhoang.identity_service.exception.ErrorCode;
import com.phamtanhoang.identity_service.repository.InvalidatedTokenRepository;
import com.phamtanhoang.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
  private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
  UserRepository userRepository;
  InvalidatedTokenRepository invalidatedTokenRepository;
  PasswordEncoder passwordEncoder;

  @NonFinal
  @Value("${jwt.signerKey}")
  protected String SINGER_KEY;

  @NonFinal
  @Value("${jwt.valid-duration}")
  protected Long VALID_DURATION;

  @NonFinal
  @Value("${jwt.refreshable-duration}")
  protected Long REFRESHABLE_DURATION;


  //verify token
  public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
    var token = request.getToken();

    boolean isValid = true;
    try {
      verifyToken(token, false);
    } catch (AppException e) {
      log.error(e.getMessage());
      isValid = false;
    }

    return IntrospectResponse.builder()
        .valid(isValid)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!authenticated) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    var token = generateToken(user);
    return AuthenticationResponse.builder()
        .token(token)
        .authenticated(true)
        .build();
  }

  public LogoutResponse logout(LogoutRequest token) throws ParseException, JOSEException {
    try {
      var signToken = verifyToken(token.getToken(), true);

      String jit = signToken.getJWTClaimsSet().getJWTID();
      Date expiration = signToken.getJWTClaimsSet().getExpirationTime();

      InvalidatedToken invalidatedToken = InvalidatedToken.builder()
          .id(jit)
          .expiryTime(expiration)
          .build();

      invalidatedTokenRepository.save(invalidatedToken);
      return LogoutResponse.builder()
          .message("Logout successfully")
          .build();

    } catch (AppException e) {
      log.error("Token already expired");
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
  }

  public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
    // check token
    var signJWT = verifyToken(request.getToken(), true);

    var jit = signJWT.getJWTClaimsSet().getJWTID();
    Date expiration = signJWT.getJWTClaimsSet().getExpirationTime();

    InvalidatedToken invalidatedToken = InvalidatedToken.builder()
        .id(jit)
        .expiryTime(expiration)
        .build();

    invalidatedTokenRepository.save(invalidatedToken);

    var username = signJWT.getJWTClaimsSet().getSubject();

    var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

    var token = generateToken(user);
    return AuthenticationResponse.builder()
        .token(token)
        .authenticated(true)
        .build();
  }

  private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
    JWSVerifier jwsVerifier = new MACVerifier(SINGER_KEY.getBytes());

    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expiryTime = (isRefresh)
        ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
          .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
        : signedJWT.getJWTClaimsSet().getExpirationTime();

    var verify = signedJWT.verify(jwsVerifier);
    if (!(verify && expiryTime.after(new Date()))) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    return signedJWT;
  }

  private String generateToken(User user) {
    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

    // build payload with claim
    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(user.getUsername()) //represent user login
        .issuer("phamtanhoang.com") //Identify who issued the token
        .issueTime(new Date())
        .expirationTime(new Date(
            Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli() // Token expires
        ))
        .jwtID(UUID.randomUUID().toString())
        .claim("scope", buildScope(user)) //add claim to do something
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

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
        stringJoiner.add("ROLE_" + role.getName());
        if (!CollectionUtils.isEmpty(role.getPermissions())) {
          for (Permission permission : role.getPermissions()) {
            stringJoiner.add(permission.getName());
          }
        }
      });
    }

    return stringJoiner.toString();
  }
}
