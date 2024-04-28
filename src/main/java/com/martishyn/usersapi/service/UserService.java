package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {

    User createNewUser(UserDto userDto);

    ResponseUserDto updateUser(Long idFromRequest, UserDto userDto);

    ResponseUserDto patchUser(Map<String, Object> fields, Long id);

    ResponseUserDto patchUser(PatchBodyWrapper patchBodyWrapper, Long id);


    boolean deleteUser(Long id);

    List<User> searchByBirthRange(LocalDate fromDate, LocalDate toDate);
}
