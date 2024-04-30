package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UserDto;

public interface UserMapper {

    User convertRequestDtoToEntity(UserDto userDto);

    ResponseUserDto convertEntityToResponseDto(User User);

    User populateEntityFromDto(User user, UserDto userDto);
}
