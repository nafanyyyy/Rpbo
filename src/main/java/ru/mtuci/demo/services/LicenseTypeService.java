package ru.mtuci.demo.services;


import org.springframework.http.ResponseEntity;
import ru.mtuci.demo.Response.LicenseTypeResponse;
import ru.mtuci.demo.model.LicenseType;

import java.util.List;

public interface LicenseTypeService {
    LicenseType getLicenseTypeById(Long id);
    LicenseType createLicenseType(LicenseType licenseType);
    void deleteLicenseType(Long id);
    List<LicenseTypeResponse> getAllLicenseTypes();
}

