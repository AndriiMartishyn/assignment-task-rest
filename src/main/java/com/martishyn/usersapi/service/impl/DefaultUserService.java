package com.martishyn.usersapi.service.impl;

import com.martishyn.usersapi.dao.UserDao;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.CreateUserDto;
import com.martishyn.usersapi.dto.user.UpdateUserDto;
import com.martishyn.usersapi.exception.ApiErrorException;
import com.martishyn.usersapi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class DefaultUserService implements UserService {
    private final UserDao userDao;
    @Value("${validation.legit.age}")
    private int legitAge;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createNewUser(CreateUserDto userDto) {
        if (!isLegitAge(userDto.getBirthDate())) {
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Person must be older than 18 years");
        }
        return userDao.save(mapToEntity(userDto));
    }

    @Override
    public UpdateUserDto updateUser(UpdateUserDto userDto, Long idFromRequest) {
        if (idFromRequest.longValue() != userDto.getId().longValue()) {
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Id mismatch between request body and path variable");
        }
        Optional<User> userFromDb = userDao.findById(idFromRequest);
        if (userFromDb.isEmpty()) {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest);
        }
        User updatedUser = User.builder()
                .id(userFromDb.get().getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .birthDate(userDto.getBirthDate())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress()).build();
        User savedUser = userDao.save(updatedUser);
        return mapUserToDto(savedUser);
    }

    private UpdateUserDto mapUserToDto(User savedUser) {
        return UpdateUserDto.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .birthDate(savedUser.getBirthDate())
                .address(savedUser.getAddress())
                .phoneNumber(savedUser.getPhoneNumber()).build();
    }

    private boolean isLegitAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= legitAge;
    }


    private User mapToEntity(CreateUserDto userDto) {
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
