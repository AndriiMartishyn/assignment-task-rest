package com.martishyn.usersapi.controller;

import com.martishyn.usersapi.dto.user.*;
import com.martishyn.usersapi.service.UserService;
import com.martishyn.usersapi.validation.Create;
import com.martishyn.usersapi.validation.Update;
import org.springframework.format.annotation.DateTimeFormat;
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

    @PostMapping()
    public ResponseEntity<?> registerUser(@RequestBody @Validated(Create.class) UserDto userDto,
                                          UriComponentsBuilder uriComponentsBuilder) {
        ResponseUserDto newUser = userService.createNewUser(userDto);
        URI location = uriComponentsBuilder
                .path("/users/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(new SingleDataResponseWrapper(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Validated(Update.class) UserDto userDto,
                                        @PathVariable Long id) {
        ResponseUserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(new SingleDataResponseWrapper(updatedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserPartially(@RequestBody UserDto userDto,
                                                 @PathVariable Long id) {
        ResponseUserDto updatedUser = userService.patchUser(id, userDto);
        return ResponseEntity.ok().body(new SingleDataResponseWrapper(updatedUser));
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
    public ResponseEntity<ListDataResponseWrapper> getAllByBirthDateRange(@RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                          @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<ResponseUserDto> responseUsers = userService.searchByBirthRange(fromDate, toDate);
        return ResponseEntity.ok(new ListDataResponseWrapper(responseUsers));
    }
}
