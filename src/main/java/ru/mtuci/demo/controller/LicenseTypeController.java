package ru.mtuci.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.Response.LicenseTypeResponse;
import ru.mtuci.demo.model.LicenseType;
import ru.mtuci.demo.services.LicenseTypeService;

import java.util.List;
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<LicenseTypeResponse> getAllLicenseTypes() {return licenseTypeService.getAllLicenseTypes();}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLicenseType(@PathVariable("id") Long id) {
        licenseTypeService.deleteLicenseType(id);
        return ResponseEntity.ok("Тип лицензии успешно удален");
    }
}
