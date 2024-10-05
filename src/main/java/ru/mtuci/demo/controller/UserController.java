package ru.mtuci.demo.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.services.UserService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping("/add")
    public void add(@RequestBody User user) {
        userService.add(user);
    }
}
