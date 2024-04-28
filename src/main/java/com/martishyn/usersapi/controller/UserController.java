package com.martishyn.usersapi.controller;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.CreateUserDto;
import com.martishyn.usersapi.dto.user.PatchBodyWrapper;
import com.martishyn.usersapi.dto.user.ResponseUserDto;
import com.martishyn.usersapi.dto.user.UpdateUserDto;
import com.martishyn.usersapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        ResponseUserDto updatedUser = userService.updateUser(userDto, id);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUserPartially(@RequestBody PatchBodyWrapper patchBodyWrapper,
                                                 @PathVariable Long id) {
//        ResponseUserDto updatedUser = userService.patchUser(patchBodyWrapper, id);
        ResponseUserDto updatedUser = userService.patchUser(patchBodyWrapper, id);
        ResponseEntity.ok().body(updatedUser);
//        return ResponseEntity.ok(updatedUser);
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllByBirthDateRange(@RequestParam LocalDate fromDate,
                                                             @RequestParam LocalDate toDate) {
        return ResponseEntity.ok(userService.searchByBirthRange(fromDate, toDate));
    }
}
