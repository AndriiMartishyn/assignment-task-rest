package com.martishyn.usersapi.service.impl;

import com.martishyn.usersapi.dao.UserDao;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {
    private final UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createNewUser(UserDto userDto) {
        User userToSave = mapToEntity(userDto);
        return userDao.save(userToSave);
    }

    private User mapToEntity(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .birthDate(userDto.getBirthDate())
                .address(userDto.getAddress())
                .phoneNumber(userDto.getPhoneNumber())
                .build();
    }
}
