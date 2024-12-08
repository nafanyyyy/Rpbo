package ru.mtuci.demo.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.exception.DeviceNotFoundException;
import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.DeviceRepository;
import ru.mtuci.demo.services.LicenseService;
import ru.mtuci.demo.services.UserService;
import ru.mtuci.demo.services.impl.LicenseResponse;
import ru.mtuci.demo.ticket.Ticket;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/licenses")
@RestController
public class LicenseController {
    private final LicenseService licenseService;
    private final DeviceRepository deviceRepository;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<License> getAll() {
        return licenseService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<LicenseResponse>  createLicense(
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

    @GetMapping("/info")
    public ResponseEntity<?> getLicenseInfo(@RequestParam String deviceInfo, HttpServletRequest httpRequest) {
        User authenticatedUser = getAuthenticatedUser(httpRequest);

        Device device = deviceRepository.findByMac(deviceInfo)
                .orElseThrow(() -> new DeviceNotFoundException("Устройство не найдено"));

        License activeLicensesForDevice = licenseService.getActiveLicensesForDevice(device, authenticatedUser);

        Ticket ticket = new Ticket();
        ticket = ticket.generateTicket(activeLicensesForDevice, device, getAuthenticatedUser(httpRequest).getId());

        return ResponseEntity.ok(ticket);
    }
    private User getAuthenticatedUser(HttpServletRequest httpRequest) {
        return userService.getUserByJwt(httpRequest);
    }
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