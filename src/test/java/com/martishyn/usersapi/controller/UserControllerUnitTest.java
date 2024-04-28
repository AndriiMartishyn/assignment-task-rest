package com.martishyn.usersapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.CreateUserDto;
import com.martishyn.usersapi.dto.user.PatchBodyWrapper;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UpdateUserDto;
import com.martishyn.usersapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerUnitTest {
    private static final String RESOURCE_ENDPOINT = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final User validUser = new User(11L, "Andrii", "Martishyn", "myemail@gmail.com", LocalDate.of(1995, 1, 29), "zelena", "991199");
    private final User invalidUser = new User(null, "", "", "q11", LocalDate.now().plusDays(1), "", "");
    private final CreateUserDto validCreateDto = new CreateUserDto("test@gmail.com", "andrii", "martishyn", LocalDate.of(1995, 1, 29), "", "");
    private final CreateUserDto invalidCreateDto = new CreateUserDto("", "", "", LocalDate.now().plusDays(1), "", "");
    private final UpdateUserDto validUpdateDto = new UpdateUserDto(11L, "test@gmail.com", "Andrii", "Mart", LocalDate.of(1997, 1, 1), "", "");
    private final UpdateUserDto invalidUpdateDto = new UpdateUserDto(null, "123311", "", "", LocalDate.of(1997, 1, 1), "", "");
    private final ResponseUserDto responseDto = new ResponseUserDto(11L, "myemail@gmail.com", "Andrii", "Martishyn", LocalDate.of(1995, 1, 29), "zelena", "991199");
    private final Map<String, Object> patchRequest = new HashMap<>();
    private final PatchBodyWrapper patchBodyWrapper = new PatchBodyWrapper();
    @Test
    public void shouldReturnCreatedWithLocationWhenCreate_WithValidInput() throws Exception {
        when(userService.createNewUser(any())).thenReturn(validUser);
        this.mockMvc.perform(post(RESOURCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/users/11")));

        ArgumentCaptor<CreateUserDto> userCaptor = ArgumentCaptor.forClass(CreateUserDto.class);
        verify(userService, times(1)).createNewUser(userCaptor.capture());
        assertThat(userCaptor.getValue().getFirstName()).isEqualTo("andrii");
        assertThat(userCaptor.getValue().getLastName()).isEqualTo("martishyn");
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("test@gmail.com");
        assertThat(userCaptor.getValue().getBirthDate()).isEqualTo(LocalDate.of(1995, 1, 29));
    }

    @Test
    public void shouldReturnBadResponseWithDetailsWhenCreate_WithInvalidInput() throws Exception {
        this.mockMvc.perform(post(RESOURCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Validation Error")))
                .andExpect(jsonPath("$.subErrors.length()").value(4));

        ArgumentCaptor<CreateUserDto> userCaptor = ArgumentCaptor.forClass(CreateUserDto.class);
        verify(userService, never()).createNewUser(userCaptor.capture());
    }

    @Test
    public void shouldReturnOKWhenUpdate_WithValidInput() throws Exception {
        ResponseUserDto responseUser = ResponseUserDto.builder().id(12L).firstName("updatedFN").lastName("updatedLN").build();
        when(userService.updateUser(validUpdateDto, 5L)).thenReturn(responseUser);
        this.mockMvc.perform(put(RESOURCE_ENDPOINT + "/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.firstName").value(containsString("updatedFN")))
                .andExpect(jsonPath("$.lastName").value(containsString("updatedLN")));

        ArgumentCaptor<UpdateUserDto> userCaptor = ArgumentCaptor.forClass(UpdateUserDto.class);
        verify(userService, times(1)).updateUser(userCaptor.capture(), eq(5L));
        assertThat(userCaptor.getValue().getId()).isEqualTo(11);
        assertThat(userCaptor.getValue().getFirstName()).isEqualTo("Andrii");
        assertThat(userCaptor.getValue().getLastName()).isEqualTo("Mart");
    }

    @Test
    public void shouldReturnBadResponseWhenUpdate_WithInvalidInput() throws Exception {
        this.mockMvc.perform(put(RESOURCE_ENDPOINT + "/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Validation Error")))
                .andExpect(jsonPath("$.subErrors.length()").value(4));

        ArgumentCaptor<UpdateUserDto> userCaptor = ArgumentCaptor.forClass(UpdateUserDto.class);
        verify(userService, never()).updateUser(userCaptor.capture(), eq(5L));
    }

    @Test
    public void shouldReturnOKWhenPartialUpdate_WithValidInput() throws Exception {
        Long requestId = 5L;
        patchBodyWrapper.addPatchBody("id", 5);
        when(userService.patchUser(patchBodyWrapper, requestId)).thenReturn(responseDto);
        this.mockMvc.perform(patch(RESOURCE_ENDPOINT + "/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchBodyWrapper)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.firstName").value(containsString("Andrii")))
                .andExpect(jsonPath("$.lastName").value(containsString("Martishyn")))
                .andExpect(jsonPath("$.email").value(containsString("myemail@gmail.com")))
                .andExpect(jsonPath("$.birthDate").value(containsString(LocalDate.of(1995, 1, 29).toString())))
                .andExpect(jsonPath("$.address").value(containsString("zelena")))
                .andExpect(jsonPath("$.phoneNumber").value(containsString("991199")));

        verify(userService, times(1)).patchUser(patchBodyWrapper, requestId);
    }


    @Test
    public void shouldReturnNotFoundResponseWhenEntityNotDeleted() throws Exception {
        long requestId = 0;
        when(userService.deleteUser(requestId)).thenReturn(false);
        this.mockMvc.perform(delete(RESOURCE_ENDPOINT + "/{id}", requestId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNoContentResponseWhenEntityDeleted() throws Exception {
        long requestId = 0;
        when(userService.deleteUser(requestId)).thenReturn(true);
        this.mockMvc.perform(delete(RESOURCE_ENDPOINT + "/{id}", requestId))
                .andExpect(status().isNoContent());
    }
}
