package ru.mtuci.demo.authorization;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginRequest {
    private String login;
    private String password;
}