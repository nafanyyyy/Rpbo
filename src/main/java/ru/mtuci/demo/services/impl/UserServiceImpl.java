package ru.mtuci.demo.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.mtuci.demo.Response.UserResponse;
import ru.mtuci.demo.configuration.JwtTokenProvider;
import ru.mtuci.demo.exception.UserAlreadyCreateException;
import ru.mtuci.demo.exception.UserException;
import ru.mtuci.demo.model.ApplicationRole;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.UserRepository;
import ru.mtuci.demo.services.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getLogin(),
                        user.getRole().name()
                ))
                .toList();
    }

    @Override
    public void add(User user) {
        if (userRepository.findByName(user.getName()).isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElse(new User());
    }

    public UserResponse getByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UserException("Пользователь с именем " + name + " не найден"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getRole().name()
        );
    }

    public void deleteUserByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UserException("Пользователь с именем " + name + " не найден"));
        userRepository.delete(user);
    }

    @Override
    public User getUserByJwt(HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UserException("JWT токен отсутствует или некорректен");
        }

        String jwt = authorizationHeader.substring(7);
        String username = jwtTokenProvider.getUsername(jwt);
        return getByLogin(username);
    }

    @Override
    public void create(String login, String name, String password) throws UserAlreadyCreateException {
        if (userRepository.findByLogin(login).isPresent()) throw new UserAlreadyCreateException(login);
        var user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(ApplicationRole.USER);
        userRepository.save(user);
    }

}