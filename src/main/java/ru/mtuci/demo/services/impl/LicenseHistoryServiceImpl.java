package ru.mtuci.demo.services.impl;

import org.springframework.stereotype.Service;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.LicenseHistory;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.LicenseHistoryRepository; // Предполагаю, что у вас есть репозиторий для LicenseHistory
import ru.mtuci.demo.services.LicenseHistoryService;

@Service
public class LicenseHistoryServiceImpl implements LicenseHistoryService {

    private final LicenseHistoryRepository licenseHistoryRepository;

    public LicenseHistoryServiceImpl(LicenseHistoryRepository licenseHistoryRepository) {
        this.licenseHistoryRepository = licenseHistoryRepository;
    }

    @Override
    public void recordLicenseChange(License license, User user, String status, String description) {

        LicenseHistory licenseHistory = new LicenseHistory();
        licenseHistory.setLicense(license);
        licenseHistory.setUser(user);
        licenseHistory.setStatus(status);
        licenseHistory.setDescription(description);

        licenseHistoryRepository.save(licenseHistory);
    }
}
