package ru.mtuci.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.demo.configuration.JwtTokenProvider;
import ru.mtuci.demo.exception.ActivationException;
import ru.mtuci.demo.exception.LicenseNotFoundException;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.services.LicenseService;
import ru.mtuci.demo.services.UserService;
import ru.mtuci.demo.Request.ActivationRequest;
import ru.mtuci.demo.ticket.Ticket;
@RequiredArgsConstructor
@RestController
@RequestMapping("/activation")
public class ActivationController {
    private final LicenseService licenseService;
    private final UserService userService;
    @PostMapping("/activate")
    public ResponseEntity<?> activateLicense(@RequestBody ActivationRequest request, HttpServletRequest httpRequest) {
        User user = userService.getUserByJwt(httpRequest);

        try {
            Ticket response = licenseService.activateLicense(request, user);
            return ResponseEntity.ok(response);
        } catch (LicenseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ActivationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

