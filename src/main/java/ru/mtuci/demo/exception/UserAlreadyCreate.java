package ru.mtuci.demo.exception;

public class UserAlreadyCreate extends RuntimeException {
    public UserAlreadyCreate(String email) {
        super("User with email " + email + " already exists");
    }

}