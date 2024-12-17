package ru.mtuci.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LicenseNotFoundException.class)
    public ResponseEntity<?> handleLicenseNotFoundException(LicenseNotFoundException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ActivationException.class)
    public ResponseEntity<?> handleActivationException(ActivationException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LicenseRenewalException.class)
    public ResponseEntity<?> handleLicenseRenewalException(LicenseRenewalException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(buildErrorResponse(errors.toString(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return new ResponseEntity<>(buildErrorResponse("Произошла непредвиденная ошибка: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyCreateException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyCreateException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<?> handleProductAlreadyExistsException(ProductException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }

    private Map<String, Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("Время и дата", LocalDateTime.now());
        errorDetails.put("Статус", status.value());
        errorDetails.put("Ошибка", status.getReasonPhrase());
        errorDetails.put("Сообщение", message);
        return errorDetails;
    }
}