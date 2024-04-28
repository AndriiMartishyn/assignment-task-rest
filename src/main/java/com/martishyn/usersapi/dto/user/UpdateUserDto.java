package com.martishyn.usersapi.dto.user;

import com.martishyn.usersapi.validation.Age;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class UpdateUserDto{
    @NotNull
    private Long id;
    @Email
    private String email;
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
    @Past
    @Age
    @NotNull
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}