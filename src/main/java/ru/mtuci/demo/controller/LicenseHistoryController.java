package ru.mtuci.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.Response.LicenseHistoryResponse;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.LicenseHistory;
import ru.mtuci.demo.services.LicenseHistoryService;


import java.util.List;

@RestController
@RequestMapping("/license-histories")
public class LicenseHistoryController {
    private final LicenseHistoryService licenseHistoryService;
    public LicenseHistoryController(LicenseHistoryService licenseHistoryService) {
        this.licenseHistoryService = licenseHistoryService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<LicenseHistoryResponse> getAllLicenseHistories() {
        return licenseHistoryService.getAllLicenseHistories();
    }

}