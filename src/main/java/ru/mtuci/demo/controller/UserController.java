package ru.mtuci.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.Response.UserResponse;

import ru.mtuci.demo.services.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
public class UserController {
    private final UserService userService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String name) {
        UserResponse response = userService.getByName(name);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteUserByName(@PathVariable("name") String name) {
        userService.deleteUserByName(name);
        return ResponseEntity.ok("Пользователь с именем " + name + " был успешно удален");
    }
}