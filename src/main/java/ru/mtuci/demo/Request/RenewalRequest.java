package ru.mtuci.demo.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RenewalRequest {
    private String activationCode;
}
