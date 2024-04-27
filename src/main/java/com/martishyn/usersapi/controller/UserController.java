package com.martishyn.usersapi.controller;

import com.martishyn.usersapi.dao.UserDao;
import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.OnCreate;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Validated(OnCreate.class) @RequestBody UserDto userDto,
                                        UriComponentsBuilder uriComponentsBuilder){
        User newUser = userService.createNewUser(userDto);
        URI location = uriComponentsBuilder
                .path("/users/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
