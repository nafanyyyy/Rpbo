package ru.mtuci.demo.services;

import org.springframework.stereotype.Service;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.User;
@Service
public interface LicenseHistoryService {
    void recordLicenseChange(License license, User user, String status, String description);
}

