package com.martishyn.usersapi.service.impl;

import com.martishyn.usersapi.dao.UserDao;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.exception.ApiErrorException;
import com.martishyn.usersapi.service.PatchValidationService;
import com.martishyn.usersapi.service.UserMapper;
import com.martishyn.usersapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultUserService implements UserService {

    private final UserDao userDao;

    private final UserMapper userMapper;

    private final PatchValidationService patchValidationService;

    public DefaultUserService(UserDao userDao, UserMapper userMapper,
                              PatchValidationService patchValidationService) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.patchValidationService = patchValidationService;
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
                .map(user -> userMapper.populateEntityFromDto(user, userDto))
                .orElseThrow(() -> new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest));
        User savedUser = userDao.save(updatedUser);
        return userMapper.convertEntityToResponseDto(savedUser);
    }


    @Override
    public ResponseUserDto patchUser(Long idFromRequest, UserDto userDto) {
        Optional<User> userFromDb = userDao.findById(idFromRequest);
        User patchedUser = userFromDb.map(user -> {
            var patched = patchValidationService.patchUserFromDto(userDto, userFromDb.get());
            patchValidationService.validateEntity(patched);
            return patched;
        }).orElseThrow(() -> new ApiErrorException(HttpStatus.NOT_FOUND, "No such user found with id " + idFromRequest));
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
        return allByDateRange.stream()
                .map(userMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    private void checkForIdMismatch(UserDto userDto, Long idFromRequest) {
        if (!idFromRequest.equals(userDto.getId())) {
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, "Id mismatch between request body and path variable");
        }
    }
}
