package ru.mtuci.demo.services.impl;

import org.springframework.stereotype.Service;
import ru.mtuci.demo.exception.DuplicateResourceException;
import ru.mtuci.demo.model.LicenseType;
import ru.mtuci.demo.repo.LicenseTypeRepository; // Предполагаю, что у вас есть репозиторий для LicenseType
import ru.mtuci.demo.services.LicenseTypeService;

@Service
public class LicenseTypeServiceImpl implements LicenseTypeService {

    private final LicenseTypeRepository licenseTypeRepository;
    @Override
    public LicenseType createLicenseType(LicenseType licenseType) {
        if (licenseTypeRepository.existsByName(licenseType.getName())) {
            throw new DuplicateResourceException("Тип лицензии с таким именем уже существует");
        }
        return licenseTypeRepository.save(licenseType);
    }
    public LicenseTypeServiceImpl(LicenseTypeRepository licenseTypeRepository) {
        this.licenseTypeRepository = licenseTypeRepository;
    }
    @Override
    public LicenseType getLicenseTypeById(Long id) {
        return licenseTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("License Type not found"));
    }
}

