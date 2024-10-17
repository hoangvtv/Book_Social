package com.phamtanhoang.identity_service.service;


import com.phamtanhoang.identity_service.dto.request.UserCreationRequest;
import com.phamtanhoang.identity_service.dto.response.UserResponse;
import com.phamtanhoang.identity_service.entity.User;
import com.phamtanhoang.identity_service.exception.AppException;
import com.phamtanhoang.identity_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  private UserCreationRequest request;
  private UserResponse response;
  private User user;
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

    user = User.builder()
        .id("ca98b54-0352-445a-903d-576598848d5e")
        .username("hoang_test")
        .firstName("Hoang")
        .lastName("Pham")
        .dob(dob)
        .build();

  }

  @Test
  void createUser_validRequest_success() {
    //Given
    when(userRepository.existsByUsername(anyString())).thenReturn(false);
    when(userRepository.save(any())).thenReturn(user);

    //When
    var response = userService.createUser(request);

    //Then
    assertThat(response.getId()).isEqualTo("ca98b54-0352-445a-903d-576598848d5e");
    assertThat(response.getUsername()).isEqualTo("hoang_test");
    assertThat(response.getFirstName()).isEqualTo("Hoang");
    assertThat(response.getLastName()).isEqualTo("Pham");
  }

  @Test
  void createUser_userExisted_fail() {
    //Given
    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    //When
    var exception = assertThrows(AppException.class, () -> userService.createUser(request));

    //then
    assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
  }

}
