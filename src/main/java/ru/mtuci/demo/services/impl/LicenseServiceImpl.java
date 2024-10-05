package ru.mtuci.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.repo.LicenseRepository;
import ru.mtuci.demo.services.LicenseService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;

    @Override
    public void add(License license) {
        licenseRepository.save(license);
    }

    @Override
    public List<License> getAll() {
        return licenseRepository.findAll();
    }
}
