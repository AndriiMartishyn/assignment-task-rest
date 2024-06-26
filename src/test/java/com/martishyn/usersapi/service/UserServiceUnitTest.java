package com.martishyn.usersapi.service;

import com.martishyn.usersapi.dao.UserDao;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.exception.ApiErrorException;
import com.martishyn.usersapi.service.impl.DefaultUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    private static final long USER_LOOKUP_ID = 11L;

    private static UserDao userDao;

    private static UserMapper userMapper;

    private static UserService userService;

    private static PatchValidationService patchValidationService;

    private static User user;

    private static UserDto userDto;

    private static UserDto updateUserDto;


    private static ResponseUserDto responseDto;

    @BeforeAll
    public static void setUp(){
        userDao = mock(UserDao.class);
        userMapper = mock(UserMapper.class);
        patchValidationService = mock(PatchValidationService.class);
        userService = new DefaultUserService(userDao, userMapper, patchValidationService);
        user = new User(11L, "Andrii", "Martishyn", "test@gmail.com", LocalDate.of(1995, 1, 29), "zelena", "991199");
        userDto = new UserDto("test@gmail.com", "Andrii", "Martishyn", LocalDate.of(1995, 1, 29), "zelena", "991199");
        updateUserDto = new UserDto(11L, "test@gmail.com", "Andrii", "Martishyn", LocalDate.of(1995, 1, 29), "zelena", "991199");
        responseDto = new ResponseUserDto(11L, "test@gmail.com", "Andrii", "Martishyn", LocalDate.of(1995, 1, 29), "zelena", "991199");
    }

    @Test
    public void shouldReturnResponseDtoWhenCreatingUser() {
        when(userMapper.convertRequestDtoToEntity(userDto)).thenReturn(user);
        when(userDao.save(user)).thenReturn(user);
        when(userMapper.convertEntityToResponseDto(user)).thenReturn(responseDto);

        ResponseUserDto newUser = userService.createNewUser(userDto);
        assertNotNull(newUser);
        assertEquals(11L, newUser.getId());
        assertEquals("Andrii", newUser.getFirstName());
        assertEquals("Martishyn", newUser.getLastName());
        assertEquals("test@gmail.com", newUser.getEmail());
        assertEquals(LocalDate.of(1995, 1, 29), newUser.getBirthDate());
        assertEquals("zelena", newUser.getAddress());
        assertEquals("991199", newUser.getPhoneNumber());
    }

    @Test
    public void shouldThrowExceptionWhileUpdateWhenIdMismatch() {
        UserDto userDto = UserDto.builder().id(2L).build();
        assertThrows(ApiErrorException.class, () -> userService.updateUser(1L, userDto),
                "Id mismatch between request body and path variable");
    }

    @Test
    public void shouldThrowExceptionWhileUpdateWhenUserNotFound() {
        long id = 2L;
        when(userDao.findById(id)).thenReturn(Optional.empty());
        UserDto userDto = UserDto.builder().id(id).build();
        assertThrows(ApiErrorException.class, () -> userService.updateUser(id, userDto),
                "No such user found with id " + id);
    }

    @Test
    public void shouldReturnResponseDtoWhenUpdatingUser() {
        when(userDao.findById(USER_LOOKUP_ID)).thenReturn(Optional.of(user));
        when(userMapper.populateEntityFromDto(any(), any())).thenReturn(user);
        when(userDao.save(user)).thenReturn(user);
        when(userMapper.convertEntityToResponseDto(any())).thenReturn(responseDto);

        userService.updateUser(USER_LOOKUP_ID, updateUserDto);
        ResponseUserDto newUser = userService.createNewUser(userDto);
        assertNotNull(newUser);
        assertEquals(11L, newUser.getId());
        assertEquals("Andrii", newUser.getFirstName());
        assertEquals("Martishyn", newUser.getLastName());
        assertEquals("test@gmail.com", newUser.getEmail());
        assertEquals(LocalDate.of(1995, 1, 29), newUser.getBirthDate());
        assertEquals("zelena", newUser.getAddress());
        assertEquals("991199", newUser.getPhoneNumber());
    }

    @Test
    public void shouldThrowExceptionWhilePatchWhenWrongId() {
        assertThrows(ApiErrorException.class, () -> userService.patchUser(2L, null),
                "Wrong provided id or data is empty");
    }

    @Test
    public void shouldThrowExceptionWhilePatchWhenWrongPatchBody() {
        assertThrows(ApiErrorException.class, () -> userService.patchUser(-1L, userDto),
                "Wrong provided id or data is empty");
    }

    @Test
    public void shouldThrowExceptionWhilePatchWhenNotExistingUser() {
        long id = 2L;
        assertThrows(ApiErrorException.class, () -> userService.patchUser(id, null),
                "No such user found with id " + id);
    }

    @Test
    public void shouldReturnResponseDtoWhenPatchingUser() {
        when(userDao.findById(USER_LOOKUP_ID)).thenReturn(Optional.of(user));
        when(patchValidationService.patchUserFromDto(updateUserDto, user)).thenReturn(user);
        doNothing().when(patchValidationService).validateEntity(any());
        when(userDao.save(user)).thenReturn(user);
        when(userMapper.convertEntityToResponseDto(any())).thenReturn(responseDto);

        ResponseUserDto newUser = userService.patchUser(USER_LOOKUP_ID, updateUserDto);
        assertNotNull(newUser);
        assertEquals(11L, newUser.getId());
        assertEquals("Andrii", newUser.getFirstName());
        assertEquals("Martishyn", newUser.getLastName());
        assertEquals("test@gmail.com", newUser.getEmail());
        assertEquals(LocalDate.of(1995, 1, 29), newUser.getBirthDate());
        assertEquals("zelena", newUser.getAddress());
        assertEquals("991199", newUser.getPhoneNumber());
    }

    @Test
    public void shouldThrowExceptionWhileDeleteWhenNotExistingUser() {
        long id = 2L;
        when(userDao.findById(id)).thenReturn(Optional.empty());
        assertThrows(ApiErrorException.class, () -> userService.deleteUser(id),
                "No such user found with id " + id);
    }

    @Test
    public void shouldReturnTrueWhileDeleteWhenUserRemoved() {
        long id = 2L;
        when(userDao.findById(id)).thenReturn(Optional.of(user));
        when(userDao.remove(user)).thenReturn(true);

        boolean result = userService.deleteUser(id);
        assertTrue(result);
        verify(userDao, times(1)).remove(user);
    }

    @Test
    public void shouldThrowExceptionWhileGetUsersWithIncorrectBirthDateRange() {
        LocalDate fromDate = LocalDate.of(2005, 1, 29);
        LocalDate toDate = LocalDate.of(2005, 1, 27);
        assertThrows(ApiErrorException.class, () -> userService.searchByBirthRange(fromDate, toDate),
                "Date range is incorrect");
    }

    @Test
    public void shouldReturnEmptyListWhileGetUsersWhenNothingFound() {
        LocalDate fromDate = LocalDate.of(2004, 1, 29);
        LocalDate toDate = LocalDate.of(2005, 1, 27);
        when(userDao.getAllByDateRange(fromDate, toDate)).thenReturn(Collections.emptyList());

        List<ResponseUserDto> responseUserDtos = userService.searchByBirthRange(fromDate, toDate);
        assertTrue(responseUserDtos.isEmpty());
    }
}
