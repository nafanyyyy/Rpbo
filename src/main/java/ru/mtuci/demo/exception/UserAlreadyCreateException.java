package ru.mtuci.demo.exception;

public class UserAlreadyCreateException extends RuntimeException {
    public UserAlreadyCreateException(String login) {
        super("Пользователь с таким логином: " + login + " уже зарегестрирован");
    }
}