package ru.mtuci.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.model.LicenseType;
import ru.mtuci.demo.services.LicenseTypeService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/license-types")
public class LicenseTypeController {

    private final LicenseTypeService licenseTypeService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createLicenseType(@RequestBody LicenseType licenseType) {
        licenseTypeService.createLicenseType(licenseType);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("Сообщение", "Тип лицензии успешно создан"));
    }
}
