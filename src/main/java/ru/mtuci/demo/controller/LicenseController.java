package ru.mtuci.demo.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.Request.CreateLicensesRequest;
import ru.mtuci.demo.Request.RenewalRequest;
import ru.mtuci.demo.exception.ActivationException;
import ru.mtuci.demo.exception.DeviceNotFoundException;
import ru.mtuci.demo.exception.LicenseNotFoundException;
import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.DeviceRepository;
import ru.mtuci.demo.services.LicenseService;
import ru.mtuci.demo.services.UserService;
import ru.mtuci.demo.Response.LicenseResponse;
import ru.mtuci.demo.ticket.Ticket;
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
    public ResponseEntity<LicenseResponse>  createLicense(
            @RequestBody CreateLicensesRequest request) {
        return licenseService.createLicense(request);
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
        License activeLicensesForDevice = licenseService.getActiveLicensesForDevice(device, authenticatedUser);
        Ticket ticket = new Ticket();
        ticket = ticket.generateTicket(activeLicensesForDevice, device, getAuthenticatedUser(httpRequest).getId());
        return ResponseEntity.ok(ticket);
    }
    private User getAuthenticatedUser(HttpServletRequest httpRequest) {
        return userService.getUserByJwt(httpRequest);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LicenseResponse> getById(@PathVariable("id") Long id) {
        return licenseService.getById(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/key/{key}")
    public ResponseEntity<LicenseResponse> getByKey(@PathVariable("key") String key) {
        return licenseService.getByKey(key);
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