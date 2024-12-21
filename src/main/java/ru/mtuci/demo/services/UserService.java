package ru.mtuci.demo.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import ru.mtuci.demo.Response.UserResponse;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.exception.UserAlreadyCreateException;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    void add(User user);
    User getById(Long id);
    void deleteUserByName(String name);
    UserResponse getByName(String name);
    User getByLogin(String login);
    User getUserByJwt(HttpServletRequest httpRequest);
    void create(String login, String name, String password) throws UserAlreadyCreateException;
}