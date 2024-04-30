package com.martishyn.usersapi.service;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.service.impl.DefaultPatchValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PatchValidationServiceUnitTest {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private final PatchValidationService patchValidationService = new DefaultPatchValidationService();

    private static UserDto invalidUserPatch;

    private static UserDto validUserPatch;

    private static UserDto validUserPatchWithIgnoredValues;

    private static User userToUpdate;

    private static User invalidUserForValidation;


    @BeforeEach
    public void setUp() {
        createUsersForTests();
    }

    @Test
    public void shouldNotUpdateEntityWithNullOrEmptyValues() {
        User result = patchValidationService.patchUserFromDto(invalidUserPatch, userToUpdate);

        assertEquals(4L, result.getId());
        assertEquals("Andrii", result.getFirstName());
        assertEquals("Mart", result.getLastName());
        assertEquals("writetome@mail.com", result.getEmail());
        assertEquals(LocalDate.of(1995, 1, 29), result.getBirthDate());
        assertEquals("zelena", result.getAddress());
        assertEquals("388888888", result.getPhoneNumber());
    }

    @Test
    public void shouldUpdateEntityWithoutNullValuesOrEmptyFields() {
        User result = patchValidationService.patchUserFromDto(validUserPatch, userToUpdate);

        assertEquals(4L, result.getId());
        assertEquals("updated", result.getFirstName());
        assertEquals("Mart", result.getLastName());
        assertEquals("updated@gmail.com", result.getEmail());
        assertEquals(LocalDate.of(1995, 1, 29), result.getBirthDate());
        assertEquals("zelena", result.getAddress());
        assertEquals("388888888", result.getPhoneNumber());
    }

    @Test
    public void shouldUpdateEntityIgnoringNullValuesOrEmptyFields() {
        User result = patchValidationService.patchUserFromDto(validUserPatchWithIgnoredValues, userToUpdate);

        assertEquals(10L, result.getId());
        assertEquals("updated!", result.getFirstName());
        assertEquals("Mart", result.getLastName());
        assertEquals("writetome@mail.com", result.getEmail());
        assertEquals(LocalDate.of(1995, 1, 29), result.getBirthDate());
        assertEquals("zelena", result.getAddress());
        assertEquals("388888888", result.getPhoneNumber());
    }

    @Test
    public void shouldThrowExceptionWhenInvalidEntity() {
        assertThrows(ConstraintViolationException.class,
                () -> patchValidationService.validateEntity(invalidUserForValidation));
    }

    @Test
    public void shouldNotThrowExceptionWhenValidEntity() {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(userToUpdate);
        assertTrue(constraintViolations.isEmpty());

        patchValidationService.validateEntity(userToUpdate);
    }

    private static void createUsersForTests() {
        invalidUserPatch = UserDto.builder()
                .id(null)
                .firstName("")
                .lastName(null)
                .birthDate(null)
                .email("")
                .address(null)
                .phoneNumber("")
                .build();
        validUserPatch = UserDto.builder()
                .firstName("updated")
                .email("updated@gmail.com")
                .build();
        validUserPatchWithIgnoredValues = UserDto.builder()
                .id(10L)
                .firstName("updated!")
                .lastName(null)
                .email("")
                .build();
        invalidUserForValidation = User.builder()
                .id(4L)
                .firstName("")
                .lastName(null)
                .birthDate(LocalDate.now().plusDays(1))
                .email("")
                .address("zelena")
                .phoneNumber("388888888")
                .build();
        userToUpdate = User.builder()
                .id(4L)
                .firstName("Andrii")
                .lastName("Mart")
                .birthDate(LocalDate.of(1995, 1, 29))
                .email("writetome@mail.com")
                .address("zelena")
                .phoneNumber("388888888")
                .build();
    }
}
