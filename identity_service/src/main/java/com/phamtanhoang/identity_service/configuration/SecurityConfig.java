package com.phamtanhoang.identity_service.configuration;


import com.phamtanhoang.identity_service.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
// authorize in method
@EnableMethodSecurity
public class SecurityConfig {

  private final String[] PUBLIC_ENDPOINTS = {"/users", "/auth/token", "/auth/introspect"};

  @Value("${jwt.signerKey}")
  private String signerKey;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(request ->
        request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
            // authorize in endpoint
//            .requestMatchers(HttpMethod.GET,"/users").hasAuthority("ROLE_ADMIN")
//            .requestMatchers(HttpMethod.GET,"/users").hasRole(Role.ADMIN.name())
            .anyRequest().authenticated());

    // Decode and validate JWT
    httpSecurity.oauth2ResourceServer( oauth2 ->
        oauth2.jwt(jwtConfigurer ->  jwtConfigurer.decoder(jwtDecoder())
            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
            //when error, direct user to
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
    );

    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    return httpSecurity.build();
  }

  //set Prefix of ROLE (when using hasAuthority())
  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter(){
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

    return jwtAuthenticationConverter;
  }

  @Bean
  JwtDecoder jwtDecoder(){
    SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");

    return NimbusJwtDecoder
        .withSecretKey(secretKeySpec)
        .macAlgorithm(MacAlgorithm.HS512)
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(10);
  }

}