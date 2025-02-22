package ru.mtuci.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.Request.LoginRequest;
import ru.mtuci.demo.Request.RegRequest;
import ru.mtuci.demo.Response.LoginResponse;
import ru.mtuci.demo.configuration.JwtTokenProvider;
import ru.mtuci.demo.model.UserDetailsImpl;
import ru.mtuci.demo.services.UserService;


@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtProvider;
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtProvider.createToken(userDetails.getUsername(), userDetails.getAuthorities());
        LoginResponse response = new LoginResponse(
                userDetails.getUsername(),
                token
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reg")
    public ResponseEntity<?> register(@RequestBody RegRequest request) {
            userService.create(request.getLogin(), request.getName(), request.getPassword());
            return ResponseEntity.ok("Пользователь успешно зарегестрирован");
    }
}