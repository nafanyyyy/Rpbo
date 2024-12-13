package ru.mtuci.demo.services;

import org.springframework.http.ResponseEntity;
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

    List<License> getAll();

    ResponseEntity<LicenseResponse> createLicense(CreateLicensesRequest request);

    Ticket activateLicense(ActivationRequest request, User user);

    License getActiveLicensesForDevice(Device device, User user);

    License getById(Long id);

    License getByKey(String key);

    Ticket renewLicense(RenewalRequest request, User user);
}
