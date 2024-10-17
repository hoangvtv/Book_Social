package com.phamtanhoang.identity_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.response.UserResponse;
import com.phamtanhoang.identity_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

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

    Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);

    //when, then
    mockMvc.perform(MockMvcRequestBuilders
        .post("/users")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(content))
        .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code")
                .value(1000))
        .andExpect(MockMvcResultMatchers.jsonPath("result.id")
            .value("ca98b54-0352-445a-903d-576598848d5e")
    );
  }

  @Test
  void createUser_usernameInvalid_fail() throws Exception {
    // given
    request.setUsername("abc");
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String content = objectMapper.writeValueAsString(request);

    //when, then
    mockMvc.perform(MockMvcRequestBuilders
            .post("/users")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(content))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("code")
            .value(1003))
        .andExpect(MockMvcResultMatchers.jsonPath("message")
            .value("Username must be at least 4 characters")
        );
  }

}
