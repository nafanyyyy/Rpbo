package ru.mtuci.demo.services;

import jakarta.servlet.http.HttpServletRequest;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.exception.UserAlreadyCreate;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAll();
    void add(User user);
    User getById(Long id);
    User getByName(String name);
    User getByLogin(String login);
    User getUserByJwt(HttpServletRequest httpRequest);
    void create(String login, String name, String password) throws UserAlreadyCreate;
}