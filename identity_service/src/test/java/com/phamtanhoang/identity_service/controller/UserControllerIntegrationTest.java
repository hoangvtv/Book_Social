package com.phamtanhoang.identity_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
class UserControllerIntegrationTest {

  @Container
  static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

  @DynamicPropertySource
  static void configureDatasource(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    registry.add("spring.datasource.driverClassName", () ->"com.mysql.cj.jdbc.Driver");
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
  }

  @Autowired
  private MockMvc mockMvc;

  private UserCreationRequest request;
  private UserResponse response;
  private LocalDate dob;

  @BeforeEach
  public void initData() {

    dob = LocalDate.of(2001, 2, 13);

    request = new UserCreationRequest().builder()
        .username("hoang_test")
        .firstName("Hoang")
        .lastName("Pham")
        .password("12345678")
        .dob(dob)
        .build();

    response = new UserResponse().builder()
        .id("ca98b54-0352-445a-903d-576598848d5e")
        .username("hoang_test")
        .firstName("Hoang")
        .lastName("Pham")
        .dob(dob)
        .build();

  }

  @Test
  void createUser_validRequest_success() throws Exception {
    // given
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String content = objectMapper.writeValueAsString(request);

    //when, then
    var response = mockMvc.perform(MockMvcRequestBuilders
        .post("/users")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(content))
        .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code")
                .value(1000))
        .andExpect(MockMvcResultMatchers.jsonPath("result.username")
            .value("hoang_test"))
        .andExpect(MockMvcResultMatchers.jsonPath("result.firstName")
            .value("Hoang"))
        .andExpect(MockMvcResultMatchers.jsonPath("result.lastName")
            .value("Pham")

    );

    log.info("Result: {}", response.andReturn().getResponse().getContentAsString());
  }

}
