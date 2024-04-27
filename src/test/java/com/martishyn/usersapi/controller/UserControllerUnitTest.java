package com.martishyn.usersapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martishyn.usersapi.domain.User;

import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerUnitTest {
    private static final String RESOURCE_ENDPOINT = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User validUser;
    private User invalidUser;
    private UserDto validUserDto;
    private UserDto invalidUserDto;

    @BeforeEach
    public void setUp() {
        validUser = User.builder().id(11L)
                .firstName("Andrii")
                .lastName("Martishyn")
                .email("myemail@gmail.com")
                .birthDate(LocalDate.of(1995, 1, 29)).build();
        validUserDto = UserDto.builder()
                .firstName("Andrii")
                .lastName("Martishyn")
                .birthDate(LocalDate.of(1995, 1, 29))
                .email("myemail@gmail.com").build();
        invalidUser = User.builder().id(11L)
                .firstName("")
                .lastName("")
                .birthDate(LocalDate.now().plusDays(1))
                .email("").build();
        invalidUserDto = UserDto.builder()
                .firstName("")
                .lastName("")
                .email("")
                .birthDate(LocalDate.now().plusDays(1)).build();
    }

    @Test
    public void shouldReturnBadRequestWithInvalidDataWithoutAdditionalInfo() throws Exception {
        when(userService.createNewUser(invalidUserDto)).thenReturn(invalidUser);
        String requestBody = objectMapper.writeValueAsString(invalidUserDto);
        this.mockMvc.perform(post(RESOURCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnLocationUriWhenCreatingNewValidUser() throws Exception {
        when(userService.createNewUser(validUserDto)).thenReturn(validUser);
        String requestBody = objectMapper.writeValueAsString(validUserDto);
        this.mockMvc.perform(post(RESOURCE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/users/11")));
    }
}
