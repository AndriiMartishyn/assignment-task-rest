package com.martishyn.usersapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.exception.ApiErrorException;
import com.martishyn.usersapi.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerUnitTest {

    private static final long PATHVARIABLE_CONSTANT = 5L;
    
    private static  String RESOURCE_ENDPOINT = "/users";

    private static  User validUser;

    private static  User invalidUser;

    private static  UserDto validCreateDto;

    private static  UserDto invalidCreateDto;

    private static  UserDto validUpdateDto;

    private static  UserDto invalidUpdateDto;

    private static  ResponseUserDto responseDto;

    private static  UserDto patchUserDto;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp(){
        createTestData();
    }

    @Test
    public void shouldReturnCreatedWithLocationWhenCreateWithValidInput() throws Exception {
        when(userService.createNewUser(validCreateDto)).thenReturn(responseDto);

        this.mockMvc.perform(post(RESOURCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/users/11")));

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userService, times(1)).createNewUser(userCaptor.capture());
        assertThat(userCaptor.getValue().getFirstName()).isEqualTo("andrii");
        assertThat(userCaptor.getValue().getLastName()).isEqualTo("martishyn");
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("test@gmail.com");
        assertThat(userCaptor.getValue().getBirthDate()).isEqualTo(LocalDate.of(1995, 1, 29));
    }

    @Test
    public void shouldReturnBadResponseWithDetailsWhenCreateWithInvalidInput() throws Exception {
        this.mockMvc.perform(post(RESOURCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Validation Error")))
                .andExpect(jsonPath("$.subErrors.length()").value(5));

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userService, never()).createNewUser(userCaptor.capture());
    }

    @Test
    public void shouldReturnOKWhenUpdateWithValidInput() throws Exception {
        when(userService.updateUser(PATHVARIABLE_CONSTANT, validUpdateDto)).thenReturn(responseDto);

        this.mockMvc.perform(put(RESOURCE_ENDPOINT + "/{id}", PATHVARIABLE_CONSTANT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(11L))
                .andExpect(jsonPath("$.data.firstName").value(containsString("Andrii")))
                .andExpect(jsonPath("$.data.lastName").value(containsString("Martishyn")));

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userService, times(1)).updateUser(eq(PATHVARIABLE_CONSTANT), userCaptor.capture());
        assertThat(userCaptor.getValue().getId()).isEqualTo(11);
        assertThat(userCaptor.getValue().getFirstName()).isEqualTo("Andrii");
        assertThat(userCaptor.getValue().getLastName()).isEqualTo("Mart");
    }

    @Test
    public void shouldReturnBadResponseWhenUpdateWithInvalidInput() throws Exception {
        this.mockMvc.perform(put(RESOURCE_ENDPOINT + "/{id}", PATHVARIABLE_CONSTANT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Validation Error")))
                .andExpect(jsonPath("$.subErrors.length()").value(4));

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userService, never()).updateUser(eq(PATHVARIABLE_CONSTANT), userCaptor.capture());
    }

    @Test
    public void shouldReturnOKWhenPartialUpdate_WithValidInput() throws Exception {
        when(userService.patchUser(PATHVARIABLE_CONSTANT, patchUserDto)).thenReturn(responseDto);

        this.mockMvc.perform(patch(RESOURCE_ENDPOINT + "/{id}", PATHVARIABLE_CONSTANT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(11))
                .andExpect(jsonPath("$.data.firstName").value(containsString("Andrii")))
                .andExpect(jsonPath("$.data.lastName").value(containsString("Martishyn")))
                .andExpect(jsonPath("$.data.email").value(containsString("myemail@gmail.com")))
                .andExpect(jsonPath("$.data.birthDate").value(containsString(LocalDate.of(1995, 1, 29).toString())))
                .andExpect(jsonPath("$.data.address").value(containsString("zelena")))
                .andExpect(jsonPath("$.data.phoneNumber").value(containsString("991199")));

        verify(userService, times(1)).patchUser(PATHVARIABLE_CONSTANT, patchUserDto);
    }

    @Test
    public void shouldReturnBadResponseWhenUpdatePartiallyWithEmptyBody() throws Exception {
        this.mockMvc.perform(patch(RESOURCE_ENDPOINT + "/{id}", PATHVARIABLE_CONSTANT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Malformed JSON request")));
    }

    @Test
    public void shouldReturnNotFoundResponseWhenEntityNotDeleted() throws Exception {
        when(userService.deleteUser(PATHVARIABLE_CONSTANT)).thenReturn(false);

        this.mockMvc.perform(delete(RESOURCE_ENDPOINT + "/{id}", PATHVARIABLE_CONSTANT))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNoContentResponseWhenEntityDeleted() throws Exception {
        when(userService.deleteUser(PATHVARIABLE_CONSTANT)).thenReturn(true);

        this.mockMvc.perform(delete(RESOURCE_ENDPOINT + "/{id}", PATHVARIABLE_CONSTANT))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnEmptyListWhenUsersNotFound() throws Exception {
        LocalDate fromDate = LocalDate.of(2005, 1, 28);
        LocalDate toDate = LocalDate.of(2005, 1, 27);

        when(userService.searchByBirthRange(fromDate, toDate)).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get(RESOURCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnListWhenFetchingAllUsers() throws Exception {
        LocalDate fromDate = LocalDate.of(2005, 1, 28);
        LocalDate toDate = LocalDate.of(2005, 1, 27);

        when(userService.searchByBirthRange(fromDate, toDate)).thenReturn(List.of(responseDto, responseDto, responseDto, responseDto));
        this.mockMvc.perform(get(RESOURCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(4));
    }

    private static  void createTestData(){
        validUser = new User(11L, "Andrii", "Martishyn", "myemail@gmail.com", LocalDate.of(1995, 1, 29), "zelena", "991199");
        invalidUser = new User(null, "", "", "q11", LocalDate.now().plusDays(1), "", "");
        validCreateDto = new UserDto("test@gmail.com", "andrii", "martishyn", LocalDate.of(1995, 1, 29), "", "");
        invalidCreateDto = new UserDto("", "", "", LocalDate.now().plusDays(1), "", "");
        validUpdateDto = new UserDto(11L, "test@gmail.com", "Andrii", "Mart", LocalDate.of(1997, 1, 1), "", "");
        invalidUpdateDto = new UserDto(null, "123311", "", "", LocalDate.of(1997, 1, 1), "", "");
        responseDto = new ResponseUserDto(11L, "myemail@gmail.com", "Andrii", "Martishyn", LocalDate.of(1995, 1, 29), "zelena", "991199");
        patchUserDto = UserDto.builder()
                .firstName("Andrii")
                .build();
    }

}
