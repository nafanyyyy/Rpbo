package ru.mtuci.demo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.services.LicenseService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/licenses")
@RestController
public class LicenseController {
    private final LicenseService licenseService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<License> getAll() {
        return licenseService.getAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public License createLicense(
            @RequestParam Long productId,
            @RequestParam Long ownerId,
            @RequestParam Long licenseTypeId,
            @RequestBody License licenseParameters) {
        return licenseService.createLicense(productId, ownerId, licenseTypeId, licenseParameters);
    }
    @PreAuthorize("hasAuthority('modification')")
    @PostMapping("/add")
    public void add(@RequestBody License license) {
        licenseService.add(license);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public License getById(@PathVariable("id") Long id) {
        return licenseService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/key/{key}")
    public License getByKey(@PathVariable("key") String key) {
        return licenseService.getByKey(key);
    }

}