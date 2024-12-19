package ru.mtuci.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LicenseTypeResponse {
    private Long id;
    private String name;
    private Integer defaultDuration;
    private String description;
}