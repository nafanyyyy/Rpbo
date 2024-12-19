package ru.mtuci.demo.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class RenewalRequest {
    private String activationCode;
    private String deviceInfo;
}
