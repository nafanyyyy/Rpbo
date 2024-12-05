package ru.mtuci.demo.services;

import org.springframework.stereotype.Service;
import ru.mtuci.demo.model.License;

import java.util.List;

public interface LicenseService {
    void add(License license);

    List<License> getAll();

    License getById(Long id);

    License getByKey(String key);

    License createLicense(Long productId, Long ownerId, Long licenseTypeId, License licenseParameters);
}
