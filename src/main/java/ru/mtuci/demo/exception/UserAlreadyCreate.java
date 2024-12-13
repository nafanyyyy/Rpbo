package ru.mtuci.demo.exception;

public class UserAlreadyCreate extends RuntimeException {
    public UserAlreadyCreate(String login) {
        super("User with login " + login + " already exists");
    }

}