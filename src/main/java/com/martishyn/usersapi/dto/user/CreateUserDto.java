package com.martishyn.usersapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class CreateUserDto{
    @Email
    private String email;
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
    @Past
    @NotNull
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
