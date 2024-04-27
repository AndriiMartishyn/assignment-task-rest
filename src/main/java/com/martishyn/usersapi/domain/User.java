package com.martishyn.usersapi.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@Getter
@Setter
public class User {
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    private String email;
    @Past
    @NotNull
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(birthDate, user.birthDate) && Objects.equals(address, user.address) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate, address, phoneNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
