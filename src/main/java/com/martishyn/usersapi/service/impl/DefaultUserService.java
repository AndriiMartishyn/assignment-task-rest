package com.martishyn.usersapi.service.impl;

import com.martishyn.usersapi.dao.UserDao;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.exception.ApiErrorException;
import com.martishyn.usersapi.service.UserMapper;
import com.martishyn.usersapi.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.beans.FeatureDescriptor;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultUserService implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;

    public DefaultUserService(UserDao userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseUserDto createNewUser(UserDto userDto) {
        User newUser = userDao.save(userMapper.convertRequestDtoToEntity(userDto));
        return userMapper.convertEntityToResponseDto(newUser);
    }

    @Override
    public ResponseUserDto updateUser(Long idFromRequest, UserDto userDto) {
        checkForIdMismatch(userDto, idFromRequest);
        User updatedUser = userDao.findById(idFromRequest)
                .map(user -> populateEntityFromDto(user, userDto))
                .orElseThrow(() -> new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest));
        User savedUser = userDao.save(updatedUser);
        return userMapper.convertEntityToResponseDto(savedUser);
    }

    private User populateEntityFromDto(User user, UserDto userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setBirthDate(userDto.getBirthDate());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        return user;
    }

    @Override
    public ResponseUserDto patchUser(Long idFromRequest, UserDto userDto) {
        if (idFromRequest <= 0 || userDto == null) {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "Wrong provided id or data is empty");
        }
        Optional<User> userFromDb = userDao.findById(idFromRequest);
        User patchedUser = userFromDb.map(user -> patchAndValidateEntity(userDto, user))
                .orElseThrow(() -> new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest));
        User savedUser = userDao.save(patchedUser);
        return userMapper.convertEntityToResponseDto(savedUser);
    }

    @Override
    public boolean deleteUser(Long idFromRequest) {
        User userFromDb = userDao.findById(idFromRequest).orElseThrow(() ->
                new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest));
        return userDao.remove(userFromDb);
    }

    @Override
    public List<ResponseUserDto> searchByBirthRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Date range is incorrect");
        }
        List<User> allByDateRange = userDao.getAllByDateRange(fromDate, toDate);
        if (allByDateRange.isEmpty()) {
            return Collections.emptyList();
        }
        return allByDateRange.stream()
                .map(userMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    public static User patchAndValidateEntity(UserDto source, User target) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        String[] nullValues = Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null
                        || Objects.equals(wrappedSource.getPropertyValue(propertyName), ""))
                .toArray(String[]::new);
        BeanUtils.copyProperties(source, target, nullValues);
        validateUser(target);
        return target;
    }

    private static void validateUser(User user) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    private void checkForIdMismatch(UserDto userDto, Long idFromRequest) {
        if (!idFromRequest.equals(userDto.getId())) {
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Id mismatch between request body and path variable");
        }
    }
}
