package ru.mtuci.demo.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.Request.CreateLicensesRequest;
import ru.mtuci.demo.Request.RenewalRequest;
import ru.mtuci.demo.exception.DeviceNotFoundException;
import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.DeviceRepository;
import ru.mtuci.demo.services.LicenseService;
import ru.mtuci.demo.services.UserService;
import ru.mtuci.demo.Response.LicenseResponse;
import ru.mtuci.demo.ticket.Ticket;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/licenses")
@RestController
public class LicenseController {
    private final LicenseService licenseService;
    private final DeviceRepository deviceRepository;
    private final UserService userService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<LicenseResponse> getAll() {
        return licenseService.getAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<LicenseResponse> createLicense(@RequestBody CreateLicensesRequest request) {
        LicenseResponse response = licenseService.createLicense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/renew")
    public ResponseEntity<?> renewLicense(@RequestBody RenewalRequest request, HttpServletRequest httpRequest) {
        User user = getAuthenticatedUser(httpRequest);
        Ticket response = licenseService.renewLicense(request, user);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/info")
    public ResponseEntity<?> getLicenseInfo(@RequestParam String deviceInfo, HttpServletRequest httpRequest) {
        User authenticatedUser = getAuthenticatedUser(httpRequest);
        Device device = deviceRepository.findByMac(deviceInfo)
                .orElseThrow(() -> new DeviceNotFoundException("Устройство не найдено"));

        List<License> activeLicensesForDevice = licenseService.getActiveLicensesForDevice(device, authenticatedUser);
        List<Ticket> tickets = new ArrayList<>();
        for (License activeLicense : activeLicensesForDevice) {
            Ticket ticket = new Ticket();
            ticket = ticket.generateTicket(activeLicense, device, authenticatedUser.getId());
            tickets.add(ticket);
        }

        return ResponseEntity.ok(tickets);
    }
    private User getAuthenticatedUser(HttpServletRequest httpRequest) {
        return userService.getUserByJwt(httpRequest);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<License> getLicenseById(@PathVariable Long id) {
        License license = licenseService.getById(id);
        return ResponseEntity.ok(license);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/key/{key}")
    public ResponseEntity<LicenseResponse> getLicenseByKey(@PathVariable String key) {
        LicenseResponse response = licenseService.getByKey(key);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/block/{id}")
    public ResponseEntity<String> blockLicense(@PathVariable("id") Long id) {
        licenseService.blockLicense(id);
        return ResponseEntity.ok("Лицензия успешно заблокирована");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/unblock/{id}")
    public ResponseEntity<String> unblockLicense(@PathVariable("id") Long id) {
        licenseService.unblockLicense(id);
        return ResponseEntity.ok("Лицензия успешно разблокирована");
    }
}