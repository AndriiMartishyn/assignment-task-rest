package com.martishyn.usersapi.controller;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.*;
import com.martishyn.usersapi.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody @Validated(Create.class) UserDto userDto,
                                          UriComponentsBuilder uriComponentsBuilder) {
        ResponseUserDto newUser = userService.createNewUser(userDto);
        URI location = uriComponentsBuilder
                .path("/users/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Validated(Update.class) UserDto userDto,
                                        @PathVariable Long id) {
        ResponseUserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUserPartially(@RequestBody PatchBodyWrapper patchBodyWrapper,
                                                 @PathVariable Long id) {
        ResponseUserDto updatedUser = userService.patchUser(id, patchBodyWrapper);
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> getAllByBirthDateRange(@RequestParam LocalDate fromDate,
                                                             @RequestParam LocalDate toDate) {
        return ResponseEntity.ok(userService.searchByBirthRange(fromDate, toDate));
    }
}
