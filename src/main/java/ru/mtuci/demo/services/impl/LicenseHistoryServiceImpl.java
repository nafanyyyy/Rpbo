package ru.mtuci.demo.services.impl;

import org.springframework.stereotype.Service;
import ru.mtuci.demo.Response.LicenseHistoryResponse;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.LicenseHistory;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.LicenseHistoryRepository;
import ru.mtuci.demo.services.LicenseHistoryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<LicenseHistoryResponse> getAllLicenseHistories() {
        return licenseHistoryRepository.findAll().stream()
                .map(licenseHistory -> new LicenseHistoryResponse(
                        licenseHistory.getId(),
                        licenseHistory.getLicense().getLicense_id(),
                        licenseHistory.getUser().getId(),
                        licenseHistory.getStatus(),
                        licenseHistory.getChangeDate(),
                        licenseHistory.getDescription()
                ))
                .toList();
    }

}
