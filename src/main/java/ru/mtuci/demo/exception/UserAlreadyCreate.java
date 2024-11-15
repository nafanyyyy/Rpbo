package ru.mtuci.demo.exception;

public class UserAlreadyCreate extends RuntimeException {
    public UserAlreadyCreate(String login) {
        super("User with email " + login + " already exists");
    }

}