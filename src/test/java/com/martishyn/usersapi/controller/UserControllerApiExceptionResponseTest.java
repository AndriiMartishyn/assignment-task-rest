package com.martishyn.usersapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martishyn.usersapi.dto.user.PatchBodyWrapper;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.exception.ApiErrorException;
import com.martishyn.usersapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerApiExceptionResponseTest {
    private static final String RESOURCE_ENDPOINT = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserDto validUpdateDto = new UserDto(11L, "test@gmail.com", "Andrii", "Mart", LocalDate.of(1997, 1, 1), "", "");
    private final PatchBodyWrapper patchBodyWrapper = new PatchBodyWrapper();

    @Test
    public void shouldReturnBadResponseWhenUpdate_WithNotFoundUser() throws Exception {
        long requestId = 5L;
        when(userService.updateUser(requestId, validUpdateDto)).thenThrow(new ApiErrorException(HttpStatus.NOT_FOUND,
                "No such user found with id " + requestId));
        this.mockMvc.perform(put(RESOURCE_ENDPOINT + "/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(containsString("No such user found with id " + requestId)));

        verify(userService, times(1)).updateUser(requestId, validUpdateDto);
    }

    @Test
    public void shouldReturnBadResponseWhenUpdate_WithDifferentId() throws Exception {
        long requestId = 5L;
        when(userService.updateUser(requestId, validUpdateDto)).thenThrow(new ApiErrorException(HttpStatus.NOT_FOUND,
                "Id mismatch between request body and path variable"));
        this.mockMvc.perform(put(RESOURCE_ENDPOINT + "/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(containsString("Id mismatch between request body and path variable")));

        verify(userService, times(1)).updateUser(requestId, validUpdateDto);
    }

    @Test
    public void shouldReturnBadResponseWhenUpdatePartially_WithNotFoundUser() throws Exception {
        long requestId = 0;
        when(userService.patchUser(requestId, patchBodyWrapper)).thenThrow(new ApiErrorException(HttpStatus.NOT_FOUND,
                "Wrong provided id or data is empty"));
        this.mockMvc.perform(patch(RESOURCE_ENDPOINT + "/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchBodyWrapper)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(containsString("Wrong provided id or data is empty")));
    }

    @Test
    public void shouldReturnBadResponseWhenUpdatePartially_WithDifferentId() throws Exception {
        long requestId = 0;
        when(userService.patchUser(requestId, patchBodyWrapper)).thenThrow(new ApiErrorException(HttpStatus.NOT_FOUND,
                "Wrong provided id or data is empty"));
        this.mockMvc.perform(patch(RESOURCE_ENDPOINT + "/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchBodyWrapper)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(containsString("Wrong provided id or data is empty")));
    }

    @Test
    public void shouldReturnBadRequestWhenGetAll_WithInvalidDateRange() throws Exception {
        LocalDate fromDate = LocalDate.of(2005, 1, 28);
        LocalDate toDate = LocalDate.of(2005, 1, 27);

        when(userService.searchByBirthRange(fromDate, toDate)).thenThrow(new ApiErrorException(HttpStatus.BAD_REQUEST,
                "Date range is incorrect"));
        this.mockMvc.perform(get(RESOURCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.data.message").value(containsString("Date range is incorrect")));
    }


}
