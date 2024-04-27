package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.UserDto;

public interface UserService {

    User createNewUser(UserDto userDto);
}
