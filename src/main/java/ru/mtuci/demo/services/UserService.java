package ru.mtuci.demo.services;

import ru.mtuci.demo.model.User;
import ru.mtuci.demo.exception.UserAlreadyCreate;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAll();
    void add(User user);
    User getById(Long id);
    User getByName(String name);

    void create(String login, String name, String password) throws UserAlreadyCreate;
}