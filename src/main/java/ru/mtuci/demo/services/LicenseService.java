package ru.mtuci.demo.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import ru.mtuci.demo.Request.CreateLicensesRequest;
import ru.mtuci.demo.Request.RenewalRequest;
import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.Request.ActivationRequest;
import ru.mtuci.demo.Response.LicenseResponse;
import ru.mtuci.demo.ticket.Ticket;

import java.util.List;

public interface LicenseService {
    void add(License license);

    List<LicenseResponse> getAll();

    ResponseEntity<LicenseResponse> createLicense(CreateLicensesRequest request);

    ResponseEntity<LicenseResponse> getById(Long id);

    Ticket activateLicense(ActivationRequest request, User user);

    License getActiveLicensesForDevice(Device device, User user);
    void blockLicense(Long id);
    void unblockLicense(Long id);
    ResponseEntity<LicenseResponse> getByKey(String key);

    Ticket renewLicense(RenewalRequest request, User user);
}
