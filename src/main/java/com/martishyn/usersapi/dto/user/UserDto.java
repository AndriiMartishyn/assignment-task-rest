package com.martishyn.usersapi.dto.user;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Builder
public class UserDto {
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;

    @Email
    private String email;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String firstName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String lastName;
    @Past
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(email, userDto.email) && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(birthDate, userDto.birthDate) && Objects.equals(address, userDto.address) && Objects.equals(phoneNumber, userDto.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, birthDate, address, phoneNumber);
    }
}




