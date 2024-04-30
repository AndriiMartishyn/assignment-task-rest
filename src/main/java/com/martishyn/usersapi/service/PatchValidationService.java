package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.UserDto;

public interface PatchValidationService {

    User patchUserFromDto(UserDto source, User target);

    void validateEntity(User user);
}
