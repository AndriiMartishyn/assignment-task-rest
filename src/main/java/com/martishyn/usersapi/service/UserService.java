package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.CreateUserDto;
import com.martishyn.usersapi.dto.user.PatchBodyWrapper;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UpdateUserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {

    User createNewUser(CreateUserDto userDto);

    ResponseUserDto updateUser(UpdateUserDto userDto, Long idFromRequest);

    ResponseUserDto patchUser(Map<String, Object> fields, Long id);

    ResponseUserDto patchUser(PatchBodyWrapper patchBodyWrapper, Long id);


    boolean deleteUser(Long id);

    List<User> searchByBirthRange(LocalDate fromDate, LocalDate toDate);
}
