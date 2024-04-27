package com.martishyn.usersapi.controller;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.CreateUserDto;
import com.martishyn.usersapi.dto.user.UpdateUserDto;
import com.martishyn.usersapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody @Valid CreateUserDto userDto,
                                          UriComponentsBuilder uriComponentsBuilder) {
        User newUser = userService.createNewUser(userDto);
        URI location = uriComponentsBuilder
                .path("/users/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UpdateUserDto userDto,
                                        @PathVariable Long id) {
        UpdateUserDto updatedUser = userService.updateUser(userDto, id);
        return ResponseEntity.ok(updatedUser);
    }
}
