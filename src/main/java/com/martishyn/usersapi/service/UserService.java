package com.martishyn.usersapi.service;

import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UserDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    ResponseUserDto createNewUser(UserDto userDto);

    ResponseUserDto updateUser(Long idFromRequest, UserDto userDto);

    ResponseUserDto patchUser(Long id, UserDto userDto);

    boolean deleteUser(Long id);

    List<ResponseUserDto> searchByBirthRange(LocalDate fromDate, LocalDate toDate);
}
