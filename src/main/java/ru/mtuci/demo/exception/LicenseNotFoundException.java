package ru.mtuci.demo.exception;

public class LicenseNotFoundException extends RuntimeException {
    public LicenseNotFoundException(String message) {
        super(message);
    }
}
