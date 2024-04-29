package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseUserDto createNewUser(UserDto userDto);

    ResponseUserDto updateUser(Long idFromRequest, UserDto userDto);

    ResponseUserDto patchUser(Long id, PatchBodyWrapper patchBodyWrapper);

    boolean deleteUser(Long id);

    List<ResponseUserDto> searchByBirthRange(LocalDate fromDate, LocalDate toDate);
}
