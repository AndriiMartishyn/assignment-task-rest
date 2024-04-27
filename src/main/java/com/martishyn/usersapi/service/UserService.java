package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.CreateUserDto;
import com.martishyn.usersapi.dto.user.UpdateUserDto;

public interface UserService {

    User createNewUser(CreateUserDto userDto);

    UpdateUserDto updateUser(UpdateUserDto userDto, Long idFromRequest);
}
