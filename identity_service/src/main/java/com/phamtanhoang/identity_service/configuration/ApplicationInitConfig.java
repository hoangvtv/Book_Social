package com.phamtanhoang.identity_service.configuration;


import com.phamtanhoang.identity_service.entity.User;
import com.phamtanhoang.identity_service.repository.RoleRepository;
import com.phamtanhoang.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

  PasswordEncoder passwordEncoder;

  @Bean
  @ConditionalOnProperty(prefix = "spring",
      value = "datasource.driverClassName",
      havingValue = "com.mysql.cj.jdbc.Driver")
  ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
    return args -> {
      if (userRepository.findByUsername("admin").isEmpty()) {
        var roles = roleRepository.findAllById(List.of("ADMIN"));
        User user = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .roles(new HashSet<>(roles))
            .build();

        userRepository.save(user);
        log.warn("admin user has been created with default password: admin, please change it");
      }
    };
  }
}
