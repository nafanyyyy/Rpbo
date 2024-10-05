package ru.mtuci.demo.services;

import ru.mtuci.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    void add(User user);
}
