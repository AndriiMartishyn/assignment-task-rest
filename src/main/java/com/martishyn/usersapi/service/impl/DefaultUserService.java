package com.martishyn.usersapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martishyn.usersapi.dao.UserDao;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.CreateUserDto;
import com.martishyn.usersapi.dto.user.PatchBodyWrapper;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UpdateUserDto;
import com.martishyn.usersapi.exception.ApiErrorException;
import com.martishyn.usersapi.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        return userDao.save(mapToEntity(userDto));
    }

    @Override
    public ResponseUserDto updateUser(UpdateUserDto userDto, Long idFromRequest) {
        checkForIdMismatch(userDto, idFromRequest);
        Optional<User> userFromDb = userDao.findById(idFromRequest);
        if (userFromDb.isEmpty()) {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest);
        }
        User updatedUser = User.builder()
                .id(idFromRequest)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .birthDate(userDto.getBirthDate())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress()).build();
        User savedUser = userDao.save(updatedUser);
        return mapUserToResponseDto(savedUser);
    }

    @Override
    public ResponseUserDto patchUser(Map<String, Object> fields, Long idFromRequest) {
        if (idFromRequest <= 0 || fields.isEmpty()) {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "Wrong provided id or data is empty");
        }
        Optional<User> userFromDb = userDao.findById(idFromRequest);
        User patchedUser = userFromDb.map(user -> PatchAndValidateEntity(fields, user))
                .orElseThrow(() -> new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest));

        User savedUser = userDao.save(patchedUser);
        return mapUserToResponseDto(savedUser);
    }

    @Override
    public ResponseUserDto patchUser(PatchBodyWrapper patchBodyWrapper, Long id) {
        return null;
    }

    @Override
    public boolean deleteUser(Long idFromRequest) {
        User userFromDb = userDao.findById(idFromRequest).orElseThrow(() ->
                new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest));
        return userDao.remove(userFromDb);
    }

    @Override
    public List<User> searchByBirthRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)){
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Date range is incorrect");
        }
        return userDao.getAllByDateRange(fromDate, toDate);
    }

    private User PatchAndValidateEntity(Map<String, Object> fields, User user) {
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(User.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, v);
            field.setAccessible(false);
        });
        validateUser(user);
        return user;
    }

    private static void validateUser(User user) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }


    private static void checkForIdMismatch(UpdateUserDto userDto, Long idFromRequest) {
        if (!idFromRequest.equals(userDto.getId())) {
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Id mismatch between request body and path variable");
        }
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

    private ResponseUserDto mapUserToResponseDto(User savedUser) {
        return ResponseUserDto.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .birthDate(savedUser.getBirthDate())
                .address(savedUser.getAddress())
                .phoneNumber(savedUser.getPhoneNumber()).build();
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
