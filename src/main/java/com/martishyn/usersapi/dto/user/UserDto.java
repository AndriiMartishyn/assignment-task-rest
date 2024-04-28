package com.martishyn.usersapi.dto.user;

import com.martishyn.usersapi.validation.Age;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

@Builder
public class UserDto {

    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;
    @Email(groups = {Create.class, Update.class})
    private String email;
    @NotBlank(groups = {Create.class, Update.class})
    private String firstName;

    @NotBlank(groups = {Create.class, Update.class})
    private String lastName;

    @Past(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class, Update.class})
    @Age(groups = {Create.class, Update.class})
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    public UserDto() {
    }

    public UserDto(Long id, String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public UserDto(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return Objects.equals(id, userDto.id) && Objects.equals(email, userDto.email) && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(birthDate, userDto.birthDate) && Objects.equals(address, userDto.address) && Objects.equals(phoneNumber, userDto.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, birthDate, address, phoneNumber);
    }
}
