package ru.mtuci.demo.exception;

public class LicenseTypeNotFoundException extends RuntimeException {
    public LicenseTypeNotFoundException(String message) {
        super(message);
    }
}
