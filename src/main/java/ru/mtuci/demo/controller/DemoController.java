package ru.mtuci.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.model.Demo;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.LicenseRepository;
import ru.mtuci.demo.repo.UserRepository;

@Controller
@RequiredArgsConstructor
public class DemoController {
    @Autowired
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "home";
    }

    @PostMapping
    public Demo getDemo(@RequestBody Demo demo){
        return demo;
    }
}