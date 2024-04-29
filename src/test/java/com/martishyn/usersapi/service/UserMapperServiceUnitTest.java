package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.service.impl.UserMapperService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperServiceUnitTest {
    private final UserMapper userMapper = new UserMapperService();


    @Test
    public void shouldConvertEntityToDtoAndViceVersa() {
        UserDto userDto = UserDto.builder()
                .firstName("Andrii")
                .lastName("Martishyn")
                .email("test@gmail.com")
                .birthDate(LocalDate.of(1995, 1, 29))
                .address("Kyiv")
                .phoneNumber("333333").build();
        User entity = userMapper.convertRequestDtoToEntity(userDto);
        assertEquals("Andrii", entity.getFirstName());
        assertEquals("Martishyn", entity.getLastName());
        assertEquals("test@gmail.com", entity.getEmail());
        assertEquals(LocalDate.of(1995, 1, 29), entity.getBirthDate());
        assertEquals("Kyiv", entity.getAddress());
        assertEquals("333333", entity.getPhoneNumber());

        entity.setId(2L);
        ResponseUserDto responseUserDto = userMapper.convertEntityToResponseDto(entity);
        assertEquals(2L, responseUserDto.getId());
        assertEquals("Andrii", responseUserDto.getFirstName());
        assertEquals("Martishyn", responseUserDto.getLastName());
        assertEquals("test@gmail.com", responseUserDto.getEmail());
        assertEquals(LocalDate.of(1995, 1, 29), responseUserDto.getBirthDate());
        assertEquals("Kyiv", responseUserDto.getAddress());
        assertEquals("333333", responseUserDto.getPhoneNumber());
    }
}
